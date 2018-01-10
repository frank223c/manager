package com.suny.association.enums;

/**************************************
 *  Description  文件操作相关枚举
 *  @author 孙建荣
 *  @date 18-1-10.下午12:37
 *  @version 1.0
 **************************************/
public enum FileOperateEnum {

    /**
     * 文件操作相关枚举
     */
    ROW_VALUE_CONVERT_NUMBER_FAIL(800, "转换成数字失败"),
    ROW_NUM_OVERFLOW(801, "读取工作行下标溢出"),
    SHEET_NUM_OVERFLOW(802, "读取工作表下标溢出"),
    FILE_NOT_EXIST(803, "读取的文件不存在"),
    FILE_READ_FAIL(803, "读取出错"),
    FILE_NOT_SUPPORT(805, "不支持的文件"),
    FILE_EXTENSION_WARN(806, "文件名可能存在欺骗"),
    FILE_NULL(807, "文件是空的，无法读取");

    private int code;
    private String value;

    FileOperateEnum(int code, String value) {
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
