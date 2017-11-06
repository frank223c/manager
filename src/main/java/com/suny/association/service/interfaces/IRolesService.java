package com.suny.association.service.interfaces;

import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.OperationLog;
import com.suny.association.entity.po.Roles;
import com.suny.association.service.IBaseService;

import java.util.List;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:17
 */
public interface IRolesService extends IBaseService<Roles> {
    /**
     * 根据查询条件查询操作记录
     * @param roles   操作日志实体
     * @return   条件查询出来的账号
     */
    int selectCountByParam(Roles roles);
    List<Account> queryQuote(Integer roleId);
}
