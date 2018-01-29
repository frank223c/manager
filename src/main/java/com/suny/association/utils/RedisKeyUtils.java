package com.suny.association.utils;

/**
 * 生成RedisKey工具
 *
 * @author 孙建荣
 * @date 17-9-24
 */
public class RedisKeyUtils {

    private static final String SPLIT = ":";
    private static final String BIZ_TICKET = "BIZ_TICKET";
    private static final String BIZ_LOGIN_TICKET = "BIZ_LOGIN_TICKET";
    private static final String BIZ_ACCOUNT = "BIZ_ACCOUNT";
    private static final String BIZ_PERMISSION = "BIZ_PERMISSION";
    private static final String BIZ_EVENT_QUEUE = "BIZ_EVENT_QUEUE";

    public static String getTicketKey(String ticket) {
        return BIZ_TICKET + SPLIT + ticket;
    }

    public static String getLoginticket(String username) {
        return BIZ_LOGIN_TICKET + SPLIT + username;
    }

    public static String getAccountKey(String ticket) {
        return BIZ_ACCOUNT + SPLIT + ticket;
    }

    public static String getPermissionKey(String accountName) {
        return BIZ_PERMISSION + SPLIT + accountName;
    }

    public static String getEventQueueKey() {
        return BIZ_EVENT_QUEUE;
    }

}



























