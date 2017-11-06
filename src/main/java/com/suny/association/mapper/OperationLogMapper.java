package com.suny.association.mapper;

import com.suny.association.entity.po.OperationLog;
import com.suny.association.mapper.interfaces.IMapper;


/**
 * Comments:  操作mapper映射
 * @author :   孙建荣
 * Create Date: 2017/03/05 23:05
 */

public interface OperationLogMapper extends IMapper<OperationLog> {

    /**
     * 根据查询条件查询操作记录
     * @param operationLog   操作日志实体
     * @return   条件查询出来的账号
     */
    int selectCountByParam(OperationLog operationLog);
}