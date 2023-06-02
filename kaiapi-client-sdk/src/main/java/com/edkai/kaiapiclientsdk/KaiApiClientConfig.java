package com.edkai.kaiapiclientsdk;

import com.edkai.kaiapiclientsdk.client.KaiApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author liukai
 * @create 2023-02-13 - 20:02
 */
@Configuration
@ConfigurationProperties(prefix = "kaiapi.client")
@Data
@ComponentScan
public class KaiApiClientConfig {
    private String accessKey;

    private String secretKey;

    @Bean
    public KaiApiClient kaiApiClient() {
        return new KaiApiClient(accessKey, secretKey);
    }
}
