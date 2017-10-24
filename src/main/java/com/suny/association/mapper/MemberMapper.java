package com.suny.association.mapper;

import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.pojo.po.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Comments:   成员表mapper映射
 *
 * @author :   孙建荣
 *         Create Date: 2017/03/05 23:05
 */

public interface MemberMapper extends IMapper<Member> {

    /**
     * 查询冻结的协会管理员信息
     *
     * @return 协会成员信息
     */
    List<Member> selectFreezeManager();

    /**
     * 查询正常的协会管理员信息
     *
     * @return 协会成员信息
     */
    List<Member> selectNormalManager();

    /**
     * 查询冻结的普通成员信息
     *
     * @return 协会成员信息
     */
    List<Member> selectFreezeMember();

    /**
     * 查询冻结的协会管理员信息
     *
     * @return 协会成员信息
     */
    List<Member> selectNormalMember();

    /**
     * 通过协会角色ID查询拥有次角色的协会成员
     *
     * @param memberRoleId 协会成员角色
     * @return 批量协会成员信息
     */
    List<Member> selectByMemberRoleId(@Param("memberRoleId") Integer memberRoleId);

    /**
     * 查询冻结的管理员信息
     *
     * @return 协会成员信息
     */
    List<Member> selectLimitMemberRole(@Param("memberRoleId") Integer memberRoleId, @Param("memberGrade") Integer memberGrade);


}