package com.suny.association.service;

import java.util.List;
import java.util.Map;

/**
 * Comments:   通用的逻辑层方法
 * Author:   孙建荣
 * Create Date: 2017/03/07 21:53
 */
public interface IBaseService<T> {
    void insert(T t);

    void deleteById(long id);

    void update(T t);

    T selectById(long id);

    T selectByName(String name);

    int selectCount();

    List<T> selectAll();

    List<T> list(Map<Object, Object> criteriaMap);
}
