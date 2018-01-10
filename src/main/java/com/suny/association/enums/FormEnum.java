package com.suny.association.enums;

/**************************************
 *  Description  网页表单数据枚举
 *  @author 孙建荣
 *  @date 18-1-10.下午1:10
 *  @version 1.0
 **************************************/
public enum FormEnum {

    /**
     *  网页表单数据枚举
     */
    NULL_OBJ(201, "对象为空"),
    FIELD_NULL(202, "必要字段为空"),
    REPEAT_EMAIL(203, "邮箱重复"),
    REPEAT_PHONE(204, "手机号码重复"),
    MUST_CHINESE(205, "一定要是中文"),
    REPEAT_USERNAME(206, "用户名重复"),
    FIELD_LENGTH_WRONG(207, "字段的长度有错误"),
    OLD_PASSWORD_WRONG(208, "原密码错误"),
    TWICE_PASSWORD_EQUALS(209, "两次密码一样"),
    TWICE_PASSWORD_DIFFERENT(210, "两次密码不一样");

    private int code;

    private String value;

    FormEnum(int code, String value) {
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
