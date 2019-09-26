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
public class DailyRemindService {

    @Autowired
    private MobileMessageUtils mobileMessageUtils;

    /**
     * 定时提醒每天发日报
     */
    //@Scheduled(initialDelay = 2000, fixedDelay = 200000)
    @Scheduled(cron = "0 0 18,19 * * ? ")
    public void dailywork() throws  Exception{
        mobileMessageUtils.sendMessage("18860359723",String.format("请及时发送每日的日报！"));
        Thread.sleep(500);
        mobileMessageUtils.sendMessage("18937278993",String.format("请及时发送每日的日报！"));
        Thread.sleep(500);
        mobileMessageUtils.sendMessage("18638026250",String.format("请及时发送每日的日报！"));
        Thread.sleep(500);
        mobileMessageUtils.sendMessage("18721879840",String.format("请及时发送每日的日报！"));
        Thread.sleep(500);
        mobileMessageUtils.sendMessage("18637482550",String.format("请及时发送每日的日报！并发送邮件"));
        Thread.sleep(500);
        mobileMessageUtils.sendMessage("15038335982",String.format("请及时发送每日的日报！并发送邮件"));


    }


}
