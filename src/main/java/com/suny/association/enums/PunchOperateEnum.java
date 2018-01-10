package com.suny.association.enums;

/**************************************
 *  Description  考勤业务相关枚举  
 *  @author 孙建荣
 *  @date 18-1-10.下午12:48
 *  @version 1.0
 **************************************/
public enum PunchOperateEnum {
    /**
     *  考勤业务相关枚举
     */
    OPEN_PUNCH_FAIL(301, "开启考勤失败"),
    OPEN_PUNCH_SUCCESS(302, "开启考勤成功"),
    REPEAT_PUNCH(303, "重复签到"),
    TIME_NOT_REACH(304, "未到达指定时间"),
    PUNCH_SUCCESS(305, "考勤成功"),
    PUNCH_FAIL(306, "考勤失败");

    private int code;

    private String value;

    PunchOperateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }
}
