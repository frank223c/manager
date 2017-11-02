package com.suny.association.entity.vo;

/**************************************
 *  Description   前端传给服务器的普通查询条件的封装
 *  @author 孙建荣
 *  @date 17-10-24.下午6:30
 *  @version 1.0
 **************************************/
public class ConditionMap<T> {

    /**
     * 查询条件中的实体类.
     */
    private T entity;
    /**
     * 查询数据库中的起始位置,默认是从0开始查询
     */
    private int offset = 0;
    /**
     * 限制查询数据库中的数据的数量,默认是一次只查询10条
     */
    private int limit = 10;

    public ConditionMap(T entity, int offset, int limit) {
        this.entity = entity;
        this.offset = offset;
        this.limit = limit;
    }
}
