package com.suny.association.enums;

/**************************************
 *  Description  登录
 *  @author 孙建荣
 *  @date 18-1-9.下午8:37
 *  @version 1.0
 **************************************/
public enum LoginEnum {
    /**
     * 登录注销相关枚举
     */
    LOGIN_FAILURE(900, "登陆验证失败"),
    NO_LOGIN_IN(987, "没有登录"),
    REPEAT_SUBMIT(988, "重复提交"),
    LOGOUT_FAIL(989, "注销失败"),
    UNDERPASS_ERROR(990, "用户名或密码输入错误"),
    VALIDATE_CODE_ERROR(991, "验证码输入错误"),
    VALIDATE_CODE_SUCCESS(992, "验证码输入正确"),
    LOGOUT_SUCCESS(993, "注销系统成功"),
    LOGIN_SYSTEM(995, "登录成功"),
    PASSWORD_ERROR(996, "密码错误"),
    REPEAT_LOGIN(998, "重复登录"),
    UNKNOWN_ERROR(999, "系统繁忙....");


    private int code;

    private String value;

    LoginEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return code + "," + value;
    }
}
