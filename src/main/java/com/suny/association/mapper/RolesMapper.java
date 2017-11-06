package com.suny.association.mapper;

import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.Roles;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Comments:  账号角色表mapper映射接口
 * @author :   孙建荣
 * Create Date: 2017/03/05 23:05
 */

public interface RolesMapper extends IMapper<Roles> {
    /**
     * 根据查询条件查询操作记录
     * @param roles   操作日志实体
     * @return   条件查询出来的账号
     */
    int selectCountByParam(Roles roles);

    List<Account> queryQuote(@Param("roleId") Integer roleId);

}