package com.suny.association.mapper;

import com.suny.association.entity.po.Roles;
import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.entity.po.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Comments:  账号表mapper接口映射
 *
 * @author :   孙建荣
 *         Create Date: 2017/03/05 23:05
 */
public interface AccountMapper extends IMapper<Account> {

    /**
     * 批量插入简单的账号,仅限于Excel文件上传的
     *
     * @param accountList 批量账号列表
     * @return 成功插入的行数
     */
    int insertBatchSimpleAccount(List<Account> accountList);

    /**
     * 根据传入协会成员的ID查询该成员是否绑定一个用户账号
     *
     * @param memberId 协会成员的ID
     * @return 绑定的用户账号信息
     */
    Account selectMemberReference(int memberId);

    /**
     * 根据查询条件查询操作记录
     * @param account   操作日志实体
     * @return   条件查询出来的账号
     */
    int selectCountByParam(Account account);

    Account queryByPhone(Long phoneNumber);

    Account queryByMail(String email);

    Account queryQuoteByAccountId(Long accountId);

    Account queryQuoteByMemberId(Long memberId);


    Account queryByMemberId(int memberId);

    int changePassword(@Param("accountId") Long accountId, @Param("newPassword") String newPassword);
}