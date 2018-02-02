package com.suny.association.async;

/**
 * 事件类型枚举类
 *
 * @author 孙建荣 on 17-9-28.下午4:52
 */
public enum EventType {

    /**
     * 登录事件类型
     */
    LOGIN(0),
    /**
     * 系统操作日志事件类型
     */
    LOG(1);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}


























