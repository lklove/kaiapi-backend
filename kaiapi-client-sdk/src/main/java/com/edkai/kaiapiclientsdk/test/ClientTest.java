package com.edkai.kaiapiclientsdk.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.edkai.common.model.entity.InterfaceInfo;
import com.edkai.kaiapiclientsdk.User;
import com.edkai.kaiapiclientsdk.client.KaiApiClient;
import com.edkai.kaiapiclientsdk.modle.entity.Api;

public class ClientTest {
    public static void main(String[] args) {
        KaiApiClient kaiApiClient =new KaiApiClient("be46abb7f4182a41d4986f53b0f9c746","82b026cdd7ed947b183f83ab423bc7f2");
        User user =new User("zhangsan");
        String param = JSONUtil.toJsonStr(user);
        Api api= new Api();
        api.setInterfaceId(1L);
        api.setUrl("http://localhost:7921/interface/name/user");
        System.out.println(kaiApiClient.invokeByPost(param, api).body());
    }
}
