package com.suny.association.service.interfaces;

import com.suny.association.entity.po.Account;
import com.suny.association.service.IBaseService;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:09
 */

public interface IAccountService extends IBaseService<Account> {
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


    int changePassword(Long accountId, String newPassword);


}
