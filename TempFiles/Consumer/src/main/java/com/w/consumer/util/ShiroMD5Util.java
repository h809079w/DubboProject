package com.w.consumer.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class ShiroMD5Util {
    public static String getFinalPassword(String username,String password){
        String hashAlgorithName = "MD5";
        int hashIterations = 2;//加密次数
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        Object obj = new SimpleHash(hashAlgorithName, password, credentialsSalt, hashIterations);
        return obj.toString();
    }
}
