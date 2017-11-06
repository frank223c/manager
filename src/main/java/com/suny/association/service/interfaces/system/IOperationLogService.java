package com.suny.association.service.interfaces.system;

import com.suny.association.entity.po.OperationLog;
import com.suny.association.service.IBaseService;

/**
 * Comments:   操作记录业务逻辑接口
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:14
 */
public interface IOperationLogService extends IBaseService<OperationLog> {

    /**
     * 根据查询条件查询操作记录
     * @param operationLog   操作日志实体
     * @return   条件查询出来的账号
     */
    int selectCountByParam(OperationLog operationLog);
}
