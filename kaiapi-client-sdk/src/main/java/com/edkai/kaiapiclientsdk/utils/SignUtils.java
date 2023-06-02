package com.edkai.kaiapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author liukai
 * @create 2023-02-13 - 20:07
 */
public class SignUtils {
    private static final String SALT="kai-api123";
    public static String getSign(String secretKey){
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = SALT + "." + secretKey;
        return digester.digestHex(content);
    }
}
