package com.suny.association.service.interfaces.core;

import com.suny.association.pojo.po.MemberRoles;
import com.suny.association.service.IBaseService;

import java.util.List;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:14
 */
public interface IMemberRolesService extends IBaseService<MemberRoles> {
    List<MemberRoles> queryQuote(Integer roleId);
}
