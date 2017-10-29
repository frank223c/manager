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
     * 批量往数据库中插入记录,切记符合参数插入,否则报错
     *
     * @param memberList 协会成员信息列表
     * @return 协会成员信息列表
     */
    int insertBatch(List<Member> memberList);

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
     * 查询需要签到的协会成员的信息
     *
     * @param limitMemberRoleId 限制的成员角色ID
     * @param memberGrade       届级
     * @return 符合条件的签到人员
     */
    List<Member> selectLimitMemberByParam(@Param("limitMemberRoleId") Integer limitMemberRoleId, @Param("memberGrade") Integer memberGrade);


}