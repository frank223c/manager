package com.suny.association.service.interfaces.system;

import com.suny.association.entity.po.LoginHistory;
import com.suny.association.service.IBaseService;

import java.util.List;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:12
 */
public interface ILoginHistoryService extends IBaseService<LoginHistory> {
    void saveLoginLog(String username, boolean authStatus);

    List<LoginHistory> queryLoginLogByMemberId(int memberId);

//    List<LoginHistory> queryByMemberId(int memberId);
}
