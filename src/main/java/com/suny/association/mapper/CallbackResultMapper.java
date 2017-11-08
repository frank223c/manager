package com.suny.association.mapper;

import com.suny.association.entity.po.Account;
import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.entity.po.CallbackResult;

/**
 * Comments:   审批结果表mapper接口映射
 * @author :   孙建荣
 * Create Date: 2017/03/05 23:05
 */
public interface CallbackResultMapper extends IMapper<CallbackResult> {
    /**
     * 根据查询条件查询操作记录
     * @param callbackResult   考勤结果数量
     * @return   条件查询出来的考勤结果数量
     */
    int selectCountByParam(CallbackResult callbackResult);

}