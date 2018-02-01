package com.suny.association.common;

import com.suny.association.entity.po.Account;

import javax.servlet.http.HttpServletRequest;

/**************************************
 *  Description  高并发处理
 *  @author 孙建荣
 *  @date 18-1-29.下午6:07
 *  @version 1.0
 **************************************/
public class RequestHolder {
    /**
     * 处理高并发的对象
     */
    private static final ThreadLocal<Account> accountHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void add(Account account) {
        accountHolder.set(account);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static Account getAccountHolder() {
        return accountHolder.get();
    }

    public static void remove() {
        accountHolder.remove();
        requestHolder.remove();
    }


}
