package com.edkai.thrid.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lk
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
@Data
public class AliSmsConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String region;
    private String templateCode;
    private String endpointOverride;
    private String signName;
}
