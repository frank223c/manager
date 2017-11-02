package com.suny.association.service.impl.system;

import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.mapper.OperationLogMapper;
import com.suny.association.entity.po.OperationLog;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.system.IOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Comments:  操作记录业务逻辑
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:38
 */
@Service
public class OperationLogServiceImpl extends AbstractBaseServiceImpl<OperationLog> implements IOperationLogService {

    private final OperationLogMapper operationLogMapper;


    @Autowired
    public OperationLogServiceImpl(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    /*   通过名字去查询操作记录   */
    @Override
    public OperationLog selectByName(String name) {
        return operationLogMapper.selectByName(name);
    }

    /*   查询操作记录的总记录数   */
    @Override
    public int selectCount() {
        return operationLogMapper.selectCount();
    }

    /*   通过查询条件查询操作记录   */
    @Override
    public List<OperationLog> selectByParam(ConditionMap<OperationLog> conditionMap) {
        return operationLogMapper.selectByParam(conditionMap);
    }

    /*   插入一条操作记录   */
    @SystemServiceLog(description = "插入一条操作记录失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
    }
}
