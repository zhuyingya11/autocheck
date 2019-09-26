package com.gainetdb.autocheck.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 账号配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "account")
public class AccountConfig {

    /**
     * 智管账号
     */
    private String username;

    /**
     * 智管密码
     */
    private String password;
    private String phone;


}
