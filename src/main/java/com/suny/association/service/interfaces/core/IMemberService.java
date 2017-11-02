package com.suny.association.service.interfaces.core;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.entity.po.Member;
import com.suny.association.service.IBaseService;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Comments:   成员表业务逻辑接口
 *
 * @author :   孙建荣
 *         Create Date: 2017/03/07 22:12
 */
public interface IMemberService extends IBaseService<Member> {

    /**
     * 通过协会成员ID查询在用户账号信息中引用了此成员的信息
     *
     * @param memberId 协会成员ID
     * @return 协会成员信息
     */
    Member selectMemberReference(int memberId);

    /**
     * 插入一条信息并返回自动产生的主键ID
     *
     * @param member 协会成员信息
     * @return 自动产生的主键ID
     */
    int insertAndReturnId(Member member);

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
    List<Member> selectByMemberRoleId(Integer memberRoleId);


    /**
     * 读取Excel里面的数据,批量插入到数据库中
     *
     * @param file          Excel文件
     * @param fileExtension 文件扩展名
     * @return 读取出来的协会成员信息
     */
    Map<String,List<Member>> insertBatchFormFile(File file, String fileExtension);


    Boolean selectEqualsMember(Member pendingMember);

    @SystemControllerLog(description = "批量插入成员信息失败")
    @Transactional(rollbackFor = Exception.class)
    List<Member> insertBatch(List<Member> memberList);
}
