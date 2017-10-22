package com.suny.association.enums;

/**
 * 提交登录指向的URL地址类型,例如可以登录前端页面,或者是管理员页面,亦或可以是其他的页面类型
 * Created by 孙建荣 on 17-9-22.上午10:43
 */
public enum ViewType {

    /**
     * 前台工程
     */
    PORTAL(0),
    /**
     * 后台工程
     */
    BACKEND(1);

    private int value;

    ViewType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
