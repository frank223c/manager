package com.suny.association.pojo.po;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

/**
 * 登录后带有的标记
 * Created by 孙建荣 on 17-9-20.上午8:37
 */
public class LoginTicket implements Serializable {

    private int id;
    // 账号ID
    private int accountId;
    // 标记过期的时间
    private LocalTime expired;
    // 标记当前的状态,是否过期 0为有效,1为过期
    private int status;
    // 标记字符
    private String ticket;

    public LoginTicket() {
    }

    public LoginTicket(int id, int accountId, LocalTime expired, int status, String ticket) {
        this.id = id;
        this.accountId = accountId;
        this.expired = expired;
        this.status = status;
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public LocalTime getExpired() {
        return expired;
    }

    public void setExpired(LocalTime expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}



























