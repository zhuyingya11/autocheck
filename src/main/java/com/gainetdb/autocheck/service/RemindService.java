package com.gainetdb.autocheck.service;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 考勤业务类
 */
@Service
@Slf4j
public class RemindService {
 
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
     * 定时提醒每天做饭
     */
    //@Scheduled(initialDelay = 2000, fixedDelay = 200000)
    @Scheduled(cron = "0 0 7 * * ?  ")
    public void remind(){
        mobileMessageUtils.sendMessage("15890166397",String.format("亲爱的楠楠，到了给你家老朱做饭的时间了！"));
    }
    /**
     * 定时提醒每天下班
     */
    //@Scheduled(initialDelay = 2000, fixedDelay = 200000)
    @Scheduled(cron = "0 0 18 * * ?  ")
    public void offwork(){
        mobileMessageUtils.sendMessage("15890166397",String.format("亲爱的楠楠，到了下班时间了，您工作一天辛苦了！"));
    }


}
