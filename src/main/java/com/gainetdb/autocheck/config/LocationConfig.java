package com.gainetdb.autocheck.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信配置类
 */
@Data
@ConfigurationProperties(prefix = "location")
@Component
public class LocationConfig {


    public String lat;
    public String lng;
    public String shortNo;
    public String about;


}
