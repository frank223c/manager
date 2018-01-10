package com.suny.association.enums;

/**
 * Comments:  基础错误代码枚举类
 * @author :   孙建荣
 * Create Date: 2017/03/08 18:10
 */
public enum CommonEnum {

    /**
     * 基础代码枚举类
     */
    ADD_SUCCESS(102, "添加成功"),
    DELETE_SUCCESS(103, "删除成功"),
    UPDATE_SUCCESS(104, "更新成功"),
    SELECT_SUCCESS(105, "查询成功"),
    ADD_FAILURE(106, "添加失败"),
    DELETE_FAILURE(107, "删除失败"),
    UPDATE_FAILURE(108, "更新失败"),
    SELECT_FAILURE(109, "查询失败"),
    REPEAT_ADD(110, "重复添加"),
    SYSTEM_LIMIT(111, "系统限制，不允许操作"),
    ADD_FAIL_ALL_NULL(112, "数据为空，全部插入失败"),
    ADD_SUCCESS_PART_OF(113, "批量插入部分成功"),
    ADD_SUCCESS_ALL(114, "数据全部插入成功"),
    HAVE_QUOTE(115, "存在引用"),
    LIMIT_MEMBER_MANAGER(116, "部门角色太低"),
    MALICIOUS_OPERATION(117, "恶意操作");

    int code;
    String value;

    CommonEnum(int code, String value) {
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
        return "系统代码:" + code + "," + value;
    }


}
