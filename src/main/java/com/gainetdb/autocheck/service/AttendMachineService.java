package com.gainetdb.autocheck.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gainetdb.autocheck.config.AccountConfig;
import com.gainetdb.autocheck.config.LocationConfig;
import com.gainetdb.autocheck.enums.ResultEnum;
import com.gainetdb.autocheck.exception.DoSignException;
import com.gainetdb.autocheck.utils.DateUtils;
import com.gainetdb.autocheck.utils.MobileMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 模拟考勤机打卡
 */
@Service
@Slf4j
public class AttendMachineService {
 
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
    private String jobnum="";


    /**
     * 模拟考勤机打卡逻辑
     * @return
     */
    //@Scheduled(initialDelay = 2000, fixedDelay = 2000)
    @Scheduled(cron = "0 0 8,19 * * ?")
    public void attendMachineCheck(){
        int holiday=DateUtils.getHoliday();
        log.info("获取节假日接口返回值为{}",holiday);
        if (holiday==0||holiday==2){
            log.info("执行考勤机打卡逻辑");
            doMachineCheck();
        }
    }

    public void doMachineCheck(){
        log.info("jobnum参数为:{}",jobnum);
        Map personMap=new HashMap();
        personMap.clear();
        if (StringUtils.isEmpty(jobnum)){
            log.info("进入无参数逻辑");
          //  personMap.put("13783609631","2015120101");//毛跃民
            personMap.put("18721879840","2018110103");//徐永奇
        }
        else {
            log.info("进入带参数逻辑");
            personMap.put("15038059874",jobnum);//其他打卡都发给我
        }


        personMap.forEach((phone,ccid)->{
            int i=5;//失败计数
            try {
                String saveSignUrl=String.format("http://oa.zhiguan360.cn/api/data/post?sn=Q10191610289&requesttime=%s&sign=",System.currentTimeMillis());//打卡方法
                log.info("saveSignUrl："+saveSignUrl);
                HttpHeaders headers = new HttpHeaders();
                headers.set("Host", "oa.zhiguan360.cn");
                headers.set("Content-Type", " application/json;charset=UTF-8");
                headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
                Date dateNow=new Date();
                String clockTime="";
               // String currentTime=DateUtils.dateFormat(dateNow,"yyyy-MM-dd HH:mm:ss");
                String currentDate=DateUtils.dateFormat(dateNow,null);
                if (DateUtils.compareDate("10",String.valueOf(dateNow.getHours()),"HH")>0){
                    //上班逻辑
                    clockTime=DateUtils.dateFormat(DateUtils.randomDate(currentDate+" 08:10:00", currentDate+" 08:50:00"),"yyyy-MM-dd HH:mm:ss");
                }

                else{
                    clockTime=DateUtils.dateFormat(DateUtils.randomDate(currentDate+" 19:10:00", currentDate+" 21:50:00"),"yyyy-MM-dd HH:mm:ss");
                }

                JSONArray data=   JSONArray.parseArray("[{\"id\":\"4516821\",\"data\":\"clockin\",\"time\":\""+clockTime+"\",\"pic\":\"\",\"verify\":1,\"ccid\":\""+ccid+"\"}]");
                HttpEntity<String> formEntity = new HttpEntity<String>(data.toString(), headers);
               String result= restTemplate.postForEntity(saveSignUrl,formEntity,String.class).getBody();
                //String result="";
                log.info(String.format("模拟考勤机执行结果为：%s",result));
                mobileMessageUtils.sendMessage(phone.toString(),String.format("您的考勤成功，考勤时间:%s,工号:%s", DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"),ccid));
                Thread.sleep(500);
                mobileMessageUtils.sendMessage("15038059874",String.format("您的考勤成功，考勤时间:%s,工号:%s", DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"),ccid));
            } catch (Exception e) {
                log.error("【模拟考勤机打卡时出现异常】{}", e);
                try {
                    mobileMessageUtils.sendMessage(phone.toString(),String.format("您的考勤失败，失败时间:%s,工号:%s", DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"),ccid));
                    Thread.sleep(500);
                    mobileMessageUtils.sendMessage("15038059874",String.format("您的考勤失败，失败时间:%s,工号:%s", DateUtils.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"),ccid));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                i--;
                if (i>0){//失败了重试5次
                    attendMachineCheck();
                }

                throw  new DoSignException(ResultEnum.ERROR);
            }

        });//map结束
    }


    public void attendMachineCheckByUrl(String jobnum) {
       // log.info();
        this.jobnum=jobnum;
        attendMachineCheck();
    }


}
