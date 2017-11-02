package com.suny.association.service.interfaces.core;

import com.suny.association.entity.po.Member;
import com.suny.association.entity.po.MemberRoles;
import com.suny.association.service.IBaseService;

import java.util.List;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:14
 */
public interface IMemberRolesService extends IBaseService<MemberRoles> {
    /**
     * 通过协会角色ID去查询拥有这些角色的成员
     * @param roleId   成员ID
     * @return  拥有该角色的成员信息
     */
    List<Member> selectByMemberRoleId(Integer roleId);
}
