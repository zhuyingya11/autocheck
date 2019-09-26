package com.gainetdb.autocheck.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信配置类
 */
@Data
@ConfigurationProperties(prefix = "sms")
@Component
public class SMSConfig {


    public String appId;
    public String secrectKey;
    public String shortNo;
    public String modCode;
    public String urlGetToken;
    public String urlGetTokenTtl;
    public String urlSendMessage;

}
