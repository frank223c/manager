package com.suny.association.service.interfaces;

import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.Roles;
import com.suny.association.service.IBaseService;

import java.util.List;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:17
 */
public interface IRolesService extends IBaseService<Roles> {
    List<Account> queryQuote(Integer roleId);
}
