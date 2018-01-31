package com.suny.association.cache;

import java.time.LocalDateTime;

/**************************************
 *  Description  缓存实体类
 *  @author 孙建荣
 *  @date 18-1-29.下午3:31
 *  @version 1.0
 **************************************/
public class Cache {

    /**
     * 缓存的ID
     */
    private String key;
    /**
     * 缓存的数据
     */
    private Object value;
    /**
     * 更新的时间
     */
    private LocalDateTime updateTime;
    /**
     * 是否过期
     */
    private boolean expired;

    public Cache() {
        super();
    }

    public Cache(String key, Object value, LocalDateTime updateTime, boolean expired) {
        this.key = key;
        this.value = value;
        this.updateTime = updateTime;
        this.expired = expired;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
