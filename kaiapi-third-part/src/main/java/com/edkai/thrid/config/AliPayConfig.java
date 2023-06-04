package com.edkai.thrid.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "alipay")
@Data
public class AliPayConfig {
    private String CHARSET;
    private String SIGN_TYPE;
    private String APP_ID;
    private String PRIVATE_KEY;
    private String ALIPAY_PUBLIC_KEY;
    private String ALIPAY_GATEWAY;

    private String NotifyUrl;

    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(ALIPAY_GATEWAY, APP_ID, PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
    }

}
