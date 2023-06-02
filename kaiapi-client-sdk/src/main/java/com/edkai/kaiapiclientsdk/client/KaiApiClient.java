package com.edkai.kaiapiclientsdk.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.edkai.common.model.entity.InterfaceInfo;
import com.edkai.kaiapiclientsdk.modle.entity.Api;
import com.edkai.kaiapiclientsdk.utils.SignUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liukai
 * @create 2023-02-13 - 20:09
 */
public class KaiApiClient {
    private String accessKey;

    private String secretKey;

    private static final String REQUEST_URL="http://localhost:8090/interface";

    public KaiApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    //todo 接口调用
    // 1.参数、请求方式、请求地址
    public HttpResponse invokeByPost(String userRequestParams,Api api){
        if (api == null || StrUtil.isBlank(userRequestParams)){
            throw new RuntimeException("接口错误");
        }
        // http://localhost:7921/api/name
        String url = api.getUrl();
        String[] hostArray = url.split("/interface");
        // todo 可以hostArray【0】给网管发请求做判断
        String requestUrl=REQUEST_URL+hostArray[1];
        Map<String, String> headerMap = getHeaderMap(api.getInterfaceId().toString());
        HttpResponse httpResponse = HttpRequest.post(requestUrl)
                .addHeaders(headerMap)
                .body(userRequestParams)
                .execute();
        return httpResponse;
    }

    /**
     *
     * @param userRequestParams get请求参数必须为 xxxx=xxx&xxxxx=xx
     * @param api
     * @return
     */
    public HttpResponse invokeByGet(String userRequestParams, Api api){
        if (api == null ){
            throw new RuntimeException("接口错误");
        }
        // http://localhost:7921/api/name
        String url = api.getUrl();
        String[] hostArray = url.split("/interface");
        // todo 可以hostArray【0】给网管发请求做判断
        String requestUrl=REQUEST_URL+hostArray[1];
        if (StringUtils.isNotBlank(userRequestParams)){
            requestUrl=requestUrl+"?"+userRequestParams;
        }
        Map<String, String> headerMap = getHeaderMap(api.getInterfaceId().toString());
        HttpResponse httpResponse = HttpRequest.get(requestUrl)
                .addHeaders(headerMap).execute();
        return httpResponse;
    }

    /**
     * 创建请求头
     * @param interfaceId 接口id
     * @return
     */
    private Map<String, String> getHeaderMap(String interfaceId){
        Map<String, String> headers =new HashMap<>();
        String sign = SignUtils.getSign(secretKey);
        headers.put("accessKey",accessKey);
        headers.put("sign",sign);
        headers.put("interfaceId",interfaceId);
        return headers;
    }

}
