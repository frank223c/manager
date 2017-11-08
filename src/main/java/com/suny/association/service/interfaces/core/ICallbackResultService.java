package com.suny.association.service.interfaces.core;

import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.ApplicationMessage;
import com.suny.association.entity.po.CallbackResult;
import com.suny.association.service.IBaseService;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:11
 */
public interface ICallbackResultService extends IBaseService<CallbackResult> {
    /**
     * 根据查询条件查询操作记录
     * @param callbackResult   考勤结果数量
     * @return   条件查询出来的考勤结果数量
     */
    int selectCountByParam(CallbackResult callbackResult);

    CallbackResult makeUpCallBackResult(ApplicationMessage applicationMessage, int managerId, Boolean resultStatus);
}
