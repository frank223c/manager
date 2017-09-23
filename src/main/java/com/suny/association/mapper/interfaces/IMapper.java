package com.suny.association.mapper.interfaces;

import com.suny.association.pojo.po.Account;

import java.util.List;
import java.util.Map;

/**
 * Comments:  通用的mapper接口
 * Author:   孙建荣
 * Create Date: 2017/03/07 14:26
 */
public interface IMapper<T> {

    void insert(T t);

    int insertAndGetId(T t);

    void deleteById(long id);

    void update(T t);

    T selectById(long id);

    T selectByName(String name);

    int selectCount();

    List<T> selectAll();

    List<T> list(Map<Object, Object> criteriaMap);


}









