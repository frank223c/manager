package com.suny.association.utils;

/**
 * 生成RedisKey工具
 * Created by 孙建荣 on 17-9-24.上午8:45
 */
public class RedisKeyUtils {

    private static final String SPLIT = ":";
    private static final String BIZ_TICKET = "BIZ_TICKET";
    private static final String BIZ_ACCOUNT = "BIZ_ACCOUNT";
    private static final String BIZ_PERMISSION = "BIZ_PERMISSION";

    public static String getTicketKey(String ticket) {
        return BIZ_TICKET + SPLIT + ticket;
    }

    public static String getAccountKey(String ticket) {
        return BIZ_ACCOUNT + SPLIT + ticket;
    }

    public static String getPermissionKey(String accountName) {
        return BIZ_PERMISSION + SPLIT + accountName;
    }

}



























