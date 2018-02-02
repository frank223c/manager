package com.suny.association.utils;

/**
 * 生成RedisKey工具
 *
 * @author 孙建荣
 * @date 17-9-24
 */
public class RedisKeyUtils {

    /**
     * 切分Redis key中的标示
     */
    private static final String SPLIT = ":";
    /**
     * 防止CSRF的token
     */
    private static final String BIZ_TICKET = "BIZ_TICKET";
    /**
     * 用户登录标示ticket
     */
    private static final String BIZ_LOGIN_TICKET = "BIZ_LOGIN_TICKET";
    /**
     * 用户账号key
     */
    private static final String BIZ_ACCOUNT = "BIZ_ACCOUNT";
    /**
     * 用户权限key
     */
    private static final String BIZ_PERMISSION = "BIZ_PERMISSION";
    /**
     * 事件模型key
     */
    private static final String BIZ_EVENT_QUEUE = "BIZ_EVENT_QUEUE";

    private  RedisKeyUtils() {
    }

    public static String getTicketKey(String ticket) {
        return BIZ_TICKET + SPLIT + ticket;
    }

    public static String getLoginTicketKey(String username) {
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



























