package com.suny.association.mapper.interfaces;

import com.suny.association.entity.vo.ConditionMap;
import java.util.List;

/**
 * Comments:  通用的mapper接口
 * @author :   孙建荣
 * Create Date: 2017/03/07 14:26
 */
public interface IMapper<T> {

    /**
     *  往数据库新增一个实信息体.
     * @param t  需要新增的实体信息
     */
    void insert(T t);

    /**
     *  插入一条数据并返回一个自动产生的主键ID
     * @param t 插入的数据实体
     * @return   插入数据时自动产生的主键ID
     */
    int insertAndReturnId(T t);

    /**
     * 通过ID删除一条数据库中的信息.
     * @param id  要被删除的ID.
     */
    void deleteById(long id);

    /**
     * 更新一条数据库的数据.
     * @param t  更新的数据实体信息.
     */
    void update(T t);

    /**
     * 通过一个ID去查询数据库中的一条数据.
     * @param id  主键ID
     * @return  查询出来的数据
     */
    T selectById(long id);

    /**
     * 通过一个name属性去查询数据库中的一条数据.
     * @param name  可能为中文中的名字等等
     * @return  查询出来的数据
     */
    T selectByName(String name);

    /**
     * 查询数据库中某张表里面数据的总量.
     * @return   总数据数
     */
    int selectCount();

    /**
     * 查询数据库中某张表所有的数据,谨慎调用!
     * @return 某张表中所有的数据
     */
    List<T> selectAll();

    /**
     * 通过查询条件去查询数据库中的记录.
     * @param conditionMap   自定义的查询条件
     * @return  条件查询出来的记录
     */
    List<T> selectByParam(ConditionMap<T> conditionMap);


}









