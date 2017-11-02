package com.suny.association.utils;

import com.suny.association.exception.DecryptException;
import com.suny.association.exception.EncryptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Comments:  加密解密工具
 * @author :   孙建荣
 * Create Date: 2017/03/08 16:47
 */
public class EncryptUtil {
    private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);
    private static final String AES="AES";
    private static final String DES="DES";
    private static final Integer SEVENTH_BIT =16;
    private static final Integer EIGHT_BIT=8;
    private static final Integer TWO=2;

    /**
     * 进行MD5加密
     *
     * @param info 要加密的信息
     * @return String 加密后的字符串
     */
    public static String encryptToMD5(String info) {
        byte[] digesta = null;
        try {
            // 得到一个md5的消息摘要
            MessageDigest alga = MessageDigest.getInstance("MD5");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        // 将摘要转为字符串
        return byte2hex(digesta);
    }

    /**
     * 进行SHA加密
     *
     * @param info 要加密的信息
     * @return String 加密后的字符串
     */
    public String encryptToSHA(String info) {
        byte[] digesta = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        // 将摘要转为字符串
        return byte2hex(digesta);
    }


    /**
     * 根据一定的算法得到相应的key
     * @param src  对应的资源
     * @return  加密后的值
     */
    public String getKey(String algorithm,String src){
        if(AES.equals(algorithm)){
            return src.substring(0, 16);
        }else if(DES.equals(algorithm)){
            return src.substring(0, 8);
        }else{
            return null;
        }
    }
    /**
     * 得到AES加密的key
     * @param src   加密的字符串
     * @return  加密后的值
     */
    public String getAESKey(String src){
        return this.getKey("AES", src);
    }
    /**
     * 得到DES加密的key
     * @param src   加密的字符串
     * @return  加密后的值
     */
    public String getDESKey(String src){
        return this.getKey("DES", src);
    }
    /**
     * 创建密匙
     *
     * @param algorithm 加密算法,可用 AES,DES,DESede,Blowfish
     * @return SecretKey 秘密（对称）密钥
     */
    public SecretKey createSecretKey(String algorithm) {
        // 声明KeyGenerator对象
        KeyGenerator keygen;
        // 声明 密钥对象
        SecretKey deskey = null;
        try {
            // 返回生成指定算法的秘密密钥的 KeyGenerator 对象
            keygen = KeyGenerator.getInstance(algorithm);
            // 生成一个密钥
            deskey = keygen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        // 返回密匙
        return deskey;
    }

    /**
     * 创建一个AES的密钥
     * @return   加密后的值
     */
    public SecretKey createSecretAESKey() {
        return createSecretKey("AES");
    }

    /**
     * 创建一个DES的密钥
     * @return   加密后的值
     */
    public SecretKey createSecretDESKey() {
        return createSecretKey("DES");
    }

    /**
     * 根据相应的加密算法、密钥、源文件进行加密，返回加密后的文件
     * @param algorithm 加密算法:DES,AES
     */
    public String encrypt(String algorithm, SecretKey key, String info) {
        // 定义要生成的密文
        byte[] cipherByte = null;
        try {
            // 得到加密/解密器
            Cipher c1 = Cipher.getInstance(algorithm);
            // 用指定的密钥和模式初始化Cipher对象
            // 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
            c1.init(Cipher.ENCRYPT_MODE, key);
            // 对要加密的内容进行编码处理,
            cipherByte = c1.doFinal(info.getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        // 返回密文的十六进制形式
        return byte2hex(cipherByte);
    }

    /**
     * 根据相应的解密算法、密钥和需要解密的文本进行解密，返回解密后的文本内容
     */
    public String decrypt(String algorithm, SecretKey key, String sInfo) {
        byte[] cipherByte = null;
        try {
            // 得到加密/解密器
            Cipher c1 = Cipher.getInstance(algorithm);
            // 用指定的密钥和模式初始化Cipher对象
            c1.init(Cipher.DECRYPT_MODE, key);
            // 对要解密的内容进行编码处理
            cipherByte = c1.doFinal(hex2byte(sInfo));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new String(cipherByte);
    }

    /**
     * 根据相应的解密算法、指定的密钥和需要解密的文本进行解密，返回解密后的文本内容
     * @param algorithm 加密算法:DES,AES
     * @param sKey 这个key可以由用户自己指定 注意AES的长度为16位,DES的长度为8位
     */
    public static String decrypt(String algorithm, String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                throw new DecryptException("Key为空null");
            }
            // 判断采用AES加解密方式的Key是否为16位
            if (AES.equals(algorithm) && sKey.length() != SEVENTH_BIT) {
                throw new DecryptException("Key长度不是16位");
            }
            // 判断采用DES加解密方式的Key是否为8位
            if (DES.equals(algorithm) && sKey.length() != EIGHT_BIT) {
                throw new DecryptException("Key长度不是8位");
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original);
            } catch (Exception e) {
                throw new DecryptException("解密异常",e);
            }
        } catch (Exception ex) {
            throw new DecryptException("解密异常",ex);
        }
    }

    /**
     * 根据相应的加密算法、指定的密钥、源文件进行加密，返回加密后的文件
     * @param algorithm 加密算法:DES,AES
     * @param sKey 这个key可以由用户自己指定 注意AES的长度为16位,DES的长度为8位
     */
    public static String encrypt(String algorithm, String sSrc, String sKey) throws Exception {
        // 判断Key是否正确
        if (sKey == null) {
            throw new EncryptException("Key为空null");
        }
        // 判断采用AES加解密方式的Key是否为16位
        if (AES.equals(algorithm) && sKey.length() != SEVENTH_BIT) {
            throw new EncryptException("Key长度不是16位");
        }
        // 判断采用DES加解密方式的Key是否为8位
        if (DES.equals(algorithm) && sKey.length() != EIGHT_BIT) {
            throw new EncryptException("Key长度不是8位");
        }
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return byte2hex(encrypted);
    }

    /**
     * 采用DES随机生成的密钥进行加密
     */
    public String encryptToDES(SecretKey key, String info) {
        return encrypt("DES", key, info);
    }

    /**
     * 采用DES指定密钥的方式进行加密
     */
    public String encryptToDES(String key, String info) throws Exception {
        return encrypt("DES", info, key);
    }

    /**
     * 采用DES随机生成密钥的方式进行解密，密钥需要与加密的生成的密钥一样
     */
    public String decryptByDES(SecretKey key, String sInfo) {
        return decrypt("DES", key, sInfo);
    }

    /**
     * 采用DES用户指定密钥的方式进行解密，密钥需要与加密时指定的密钥一样
     */
    public String decryptByDES(String key, String sInfo) throws Exception {
        return decrypt("DES", sInfo, key);
    }

    /**
     * 采用AES随机生成的密钥进行加密
     */
    public String encryptToAES(SecretKey key, String info) {
        return encrypt("AES", key, info);
    }
    /**
     * 采用AES指定密钥的方式进行加密
     */
    public String encryptToAES(String key, String info) throws Exception {
        return encrypt("AES", info, key);
    }

    /**
     * 采用AES随机生成密钥的方式进行解密，密钥需要与加密的生成的密钥一样
     */
    public String decryptByAES(SecretKey key, String sInfo) {
        return decrypt("AES", key, sInfo);
    }
    /**
     * 采用AES用户指定密钥的方式进行解密，密钥需要与加密时指定的密钥一样
     */
    public String decryptByAES(String key, String sInfo) throws Exception {
        return decrypt("AES", sInfo, key);
    }

    /**
     * 十六进制字符串转化为2进制
     */
    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % TWO == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / TWO; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    /**
     * 将二进制转化为16进制字符串
     *
     * @param b 二进制字节数组
     * @return String
     */
    public static String byte2hex(byte[] b) {
        StringBuilder hs =new StringBuilder();
        String stmp;
        for (byte aB : b) {
            stmp = (Integer.toHexString(aB & 0XFF));
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        EncryptUtil encryptUtils = new EncryptUtil();
        String source = "www.putiman.com";
        logger.info("Hello经过MD5:{}",encryptToMD5(source));
        logger.info("Hello经过SHA:{}", encryptUtils.encryptToSHA(source));
        logger.info("========随机生成Key进行加解密==============");
        // 生成一个DES算法的密匙
        SecretKey key = encryptUtils.createSecretDESKey();
        String str1 = encryptUtils.encryptToDES(key, source);
        logger.info("DES加密后为:{}" , str1);
        // 使用这个密匙解密
        String str2 = encryptUtils.decryptByDES(key, str1);
        logger.info("DES解密后为：{}" , str2);

        // 生成一个AES算法的密匙
        SecretKey key1 = encryptUtils.createSecretAESKey();
        String stra = encryptUtils.encryptToAES(key1, source);
        logger.info("AES加密后为:{}", stra);
        // 使用这个密匙解密
        String strb = encryptUtils.decryptByAES(key1, stra);
        logger.info("AES解密后为：{}",strb);
        logger.info("========指定Key进行加解密==============");
        try {
            String aeskey = encryptUtils.getAESKey(encryptUtils.encryptToSHA(source));
            String deskey = encryptUtils.getDESKey(encryptUtils.encryptToSHA(source));
            logger.info(aeskey);
            logger.info(deskey);
            String str11 = encryptUtils.encryptToDES(deskey, source);
            logger.info("DES加密后为:{}",str11);
            // 使用这个密匙解密
            String str12 = encryptUtils.decryptByDES(deskey, str11);
            logger.info("DES解密后为：{}" , str12);

            // 生成一个AES算法的密匙
            String strc = encryptUtils.encryptToAES(aeskey, source);
            logger.info("AES加密后为:{}", strc);
            // 使用这个密匙解密
            String std = encryptUtils.decryptByAES(aeskey, strc);
            logger.info("AES解密后为{}：" , std);
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
        }
    }
}

