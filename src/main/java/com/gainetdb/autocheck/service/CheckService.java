package com.gainetdb.autocheck.service;

import com.gainetdb.autocheck.AutocheckApplication;
import com.gainetdb.autocheck.config.AccountConfig;
import com.gainetdb.autocheck.config.LocationConfig;
import com.gainetdb.autocheck.enums.ResultEnum;
import com.gainetdb.autocheck.exception.DoSignException;
import com.gainetdb.autocheck.utils.DateUtils;
import com.gainetdb.autocheck.utils.MobileMessageUtils;
import com.gainetdb.autocheck.utils.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 考勤业务类
 */
@Service
@Slf4j
public class CheckService {
 
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AccountConfig accountConfig;
    @Autowired
    private LocationConfig locationConfig;
    @Autowired
    private MobileMessageUtils mobileMessageUtils;
    private String phone;
    private String username;
    private String password;
    private String lat;
    private String lng;
    private String about;

    /**
     * 模拟打卡逻辑
     * @return
     */
    //@Scheduled(initialDelay = 2000, fixedDelay = 2000)
    @Scheduled(cron = "0 0 8,21,22 * * ?")
    public void check(){
        int holiday=DateUtils.getHoliday();
        log.info("获取节假日接口返回值为{}",holiday);
        if (holiday==0||holiday==2){
            log.info("执行移动打卡逻辑");
           doMobileCheck();
        }
    }

    public void doMobileCheck(){
        int i=5;//失败计数
        getOutProperty();//读取电话,登录账号密码等信息
        try {
            // int i= 1/0;
            String perLoginUrl = "http://oa.zhiguan360.cn/system/loginView.do#login";// 前置登录页面
            String loginUrl = "http://oa.zhiguan360.cn/system/login.do";//登录方法
            String saveSignUrl="http://oa.zhiguan360.cn/statistics/saveSign.do";//移动打卡方法
            HttpHeaders headers = new HttpHeaders();
            headers.set("Host", "oa.zhiguan360.cn");
            headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
            headers.set("Accept", "application/json, text/javascript, */*; q=0.01");
            headers.set("Accept-Language", "zh-CN,zh;q=0.8");

            ResponseEntity<String> exchangelogin=restTemplate.exchange(perLoginUrl, HttpMethod.GET, new HttpEntity<>(headers),
                    String.class);
            List<String > list =exchangelogin.getHeaders().get("Set-Cookie");
            String cookie=list.get(0);
            System.out.println(list.get(0));
            headers.set("Cookie",list.get(0));

            MultiValueMap<String, String> loginParams = new LinkedMultiValueMap<>();
            loginParams.set("KEYDATA", String.format("yank1%s,llk,%syank2,llk,",username,password));
            // loginParams.set("KEYDATA", "yank115036169997,llk,lizhongya0yank2,llk,");
            loginParams.set("tm", String.valueOf(new Date().getTime()));
            ResponseEntity<String> exchange = restTemplate.exchange(loginUrl, HttpMethod.POST,
                    new HttpEntity<>(loginParams, headers), String.class);
            String loginResult = exchange.getBody();
            log.info("登录结果，{}",loginResult);
            if(loginResult != null && loginResult.contains("error")){
                log.info("移动考勤结果失败：登录失败");
                mobileMessageUtils.sendMessage(phone,String.format("移动考勤结果失败：登录失败,失败时间:%s", DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss")));
                return;
            }
            headers.clear();
            System.out.println("cookie的值为:"+cookie.split(";")[0]);
            headers.set("COOKIE",cookie.split(";")[0]);
            headers.set("Content-Type","application/x-www-form-urlencoded");
            MultiValueMap<String, String> checkParams = new LinkedMultiValueMap<>();
            checkParams.set("lat", lat);
            checkParams.set("lng",lng);
            checkParams.set("about",about);
            checkParams.set("view", "check");
            ResponseEntity<String> exchangecheck = restTemplate.exchange(saveSignUrl, HttpMethod.POST,
                   new HttpEntity<>(checkParams, headers), String.class);
            String clockingResult = exchangecheck.getBody();
           // String clockingResult= "";
            log.info("移动考勤结果:{}",clockingResult);
            if(clockingResult == null || "null".equals(clockingResult)){
                mobileMessageUtils.sendMessage(phone,String.format("移动考勤失败：登录成功打开失败,失败时间:%s", DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss")));
                return ;

            }
            mobileMessageUtils.sendMessage(phone,String.format("您的移动考勤成功，考勤时间:%s", DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            log.error("【移动打卡时出现异常】{}", e);
            try {
                mobileMessageUtils.sendMessage(phone,String.format("您的考勤失败，失败时间:%s", DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss")));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            i--;
            if (i>0){//失败了重试5次
                doMobileCheck();
            }

            throw  new DoSignException(ResultEnum.ERROR);
        }
    }
    //读取配置文件优先读取外部，外部出错读取内部
    public  void getOutProperty(){
        try {
            System.out.println(System.getProperty("user.dir"));
            String path=System.getProperty("user.dir");
            PropertiesUtil propertiesUtil = new PropertiesUtil(path + "/account.properties");
            log.info("获取外部配置文件路径为:{}",path+"/account.properties");
            phone = propertiesUtil.readProperty("phone");
            username = propertiesUtil.readProperty("username");
            password = propertiesUtil.readProperty("password");
            lat = propertiesUtil.readProperty("lat");
            lng = propertiesUtil.readProperty("lng");
            about = propertiesUtil.readProperty("about");
            log.info("读取外部配置文件成功");
            log.info(String.format("读取配置参数为：username->%s,password->%s,phone->%s,lat->%s,lng->%s,about->%s",username,password,phone,lat,lng,about));
        } catch (Exception e) {
            log.error(String.format("读取外部配置文件发生异常%s",e));
            //读取外部配置文件失败再读取内部yml注入值，调试时候使用
            phone = accountConfig.getPhone();
            username =accountConfig.getUsername();
            password = accountConfig.getPassword();
            lat=locationConfig.getLat();
            lng=locationConfig.getLng();
            about=locationConfig.getAbout();
            log.info("读取内部配置文件成功");
            log.info(String.format("读取配置参数为：username->%s,password->%s,phone->%s,lat->%s,lng->%s,about->%s",username,password,phone,lat,lng,about));

        }
    }
}
