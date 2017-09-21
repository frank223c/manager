package com.suny.association.enums;

/**
 * 操作类型枚举
 * Created by 孙建荣 on 17-9-21.下午4:36
 */
public enum OperateType {
    INSERT(1),
    DELETE(2),
    UPDATE(3),
    SELECT(4);

    private int value;

    OperateType(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }
}
