package com.suny.association.enums;

/**
 * 操作类型枚举
 *
 * @author 孙建荣
 * @date 17-9-21
 */
public enum OperateType {
    /**
     * 插入操作
     */
    INSERT(1,"插入"),
    /**
     * 删除操作
     */
    DELETE(2,"删除"),
    /**
     * 更新操作
     */
    UPDATE(3,"更新"),
    /**
     * 查询操作
     */
    SELECT(4,"查询");

    private int code;

    private String value;

    OperateType(int code, String value) {
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
