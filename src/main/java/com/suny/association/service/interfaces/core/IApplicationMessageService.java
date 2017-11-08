package com.suny.association.service.interfaces.core;

import com.suny.association.entity.po.ApplicationMessage;
import com.suny.association.entity.po.CallbackResult;
import com.suny.association.service.IBaseService;

/**
 * Comments:
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:10
 */
public interface IApplicationMessageService extends IBaseService<ApplicationMessage> {
    /**
     * 根据查询条件查询总记录
     * @param applicationMessage   考勤记录实体
     * @return   条件查询出来的记录
     */
    int selectCountByParam(ApplicationMessage applicationMessage);

    void updateApplyForResult(ApplicationMessage applicationMessage, CallbackResult callbackResult);
}
