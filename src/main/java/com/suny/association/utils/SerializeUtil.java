package com.suny.association.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 序列化与反序列化工具
 *
 * @author 孙建荣
 * @date 17-9-24
 */
public class SerializeUtil {
    private static Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

    public static byte[] serialize(Object o) {
        ObjectOutputStream objectOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(o);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("{}序列化是出现了错误{}", o, e.getMessage());
        }
        return null;
    }


    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            logger.error("{}反序列化是出现了错误{}", bytes, e.getMessage());
        }
        return null;
    }

}














