package com.suny.association.utils;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Comments:   产生token，防止表单重复提交
 * @author :   孙建荣
 * Create Date: 2017/05/13 21:35
 */
public class TokenProcessor {

    /**
     * 单例设计模式，保证在内存里面只存在一个
     * 首先把类的构造函数进行私有
     * 自己创造一个类的对象
     * 对外提供一个共有的方法，返回该类的对象
     */
    private TokenProcessor() {
    }

    private static final TokenProcessor INSTANCE = new TokenProcessor();

    public static TokenProcessor getInstance() {
        return INSTANCE;
    }

    public String makeToken() {
        String token = Long.toString(System.currentTimeMillis() + new Random().nextInt(99999999));
        //  加密下
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] md5 = md.digest(token.getBytes());
            // base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(md5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }
}













