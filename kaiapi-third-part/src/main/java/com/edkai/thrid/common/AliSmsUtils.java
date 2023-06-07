package com.edkai.thrid.common;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.edkai.common.model.to.SmsTo;
import com.edkai.thrid.config.AliSmsConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author lk
 */
@Component
public class AliSmsUtils {
    @Resource
    private AliSmsConfig aliSmsConfig;

    public SendSmsResponse sendSms(SmsTo smsTo) throws ExecutionException, InterruptedException {
        // Configure Credentials authentication information, including ak, secret, token
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                // Please ensure that the environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET are set.
                .accessKeyId(aliSmsConfig.getAccessKeyId())
                .accessKeySecret(aliSmsConfig.getAccessKeySecret())
                //.securityToken(System.getenv("ALIBABA_CLOUD_SECURITY_TOKEN")) // use STS token
                .build());

        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region(aliSmsConfig.getRegion()) // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(aliSmsConfig.getEndpointOverride())
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();

        // Parameter settings for API request
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .templateCode(aliSmsConfig.getTemplateCode())
                .phoneNumbers(smsTo.getMobile())
                .templateParam(String.format("{\"code\":\"%s\"}",smsTo.getCode()))
                .signName(aliSmsConfig.getSignName())
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        // Synchronously get the return value of the API request
        SendSmsResponse resp = response.get();
        client.close();
        return resp;
        // Asynchronous processing of return values

        /*response.thenAccept(resp -> {


            System.out.println(new Gson().toJson(resp));
        }).exceptionally(throwable -> { // Handling exceptions
            System.out.println(throwable.getMessage());
            return null;
        });*/

        // Finally, close the client

    }
}
