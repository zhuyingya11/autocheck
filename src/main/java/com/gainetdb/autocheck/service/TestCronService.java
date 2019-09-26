package com.gainetdb.autocheck.service;

import com.gainetdb.autocheck.config.AccountConfig;
import com.gainetdb.autocheck.config.LocationConfig;
import com.gainetdb.autocheck.utils.MobileMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 考勤业务类
 */
@Service
@Slf4j
public class TestCronService {
    @Autowired
    private MobileMessageUtils mobileMessageUtils;


    /**
     * 定时提醒每天做饭
     */
    @Scheduled(initialDelay = 2000, fixedDelay = 20000)
    //@Scheduled(cron = "0 0 9,10,11,12,14,15,16,17,18,19,20,21 * * ? ")
    public void remindtest(){
        log.info("定时任务正常执行了");
       // mobileMessageUtils.sendMessage("15038059874",String.format("定时任务正常执行了！"));
    }


}
