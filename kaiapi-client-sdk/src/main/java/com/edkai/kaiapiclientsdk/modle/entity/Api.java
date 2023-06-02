package com.edkai.kaiapiclientsdk.modle.entity;

/**
 * 调用接口类
 * @author lk
 */
public class Api {
    /**
     * 接口id
     */
    private Long interfaceId;
    /**
     * 接口url;
     */
    private String url;

    public Long getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(Long interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
