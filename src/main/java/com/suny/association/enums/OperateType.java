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
    INSERT(1),
    /**
     * 删除操作
     */
    DELETE(2),
    /**
     * 更新操作
     */
    UPDATE(3),
    /**
     * 查询操作
     */
    SELECT(4);

    private int value;

    OperateType(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }
}
