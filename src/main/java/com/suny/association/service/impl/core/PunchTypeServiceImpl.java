package com.suny.association.service.impl.core;

import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.mapper.PunchTypeMapper;
import com.suny.association.pojo.po.ApplicationMessage;
import com.suny.association.pojo.po.PunchType;
import com.suny.association.pojo.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.core.IPunchTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Comments:  考勤类型业务逻辑
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:40
 */
@Service
public class PunchTypeServiceImpl extends AbstractBaseServiceImpl<PunchType> implements IPunchTypeService {

    private PunchTypeMapper punchTypeMapper;

    @Autowired
    public PunchTypeServiceImpl(PunchTypeMapper punchTypeMapper) {
        this.punchTypeMapper = punchTypeMapper;
    }

    public PunchTypeServiceImpl() {
    }

    /*   更细一条考勤类型信息失败   */
    @SystemServiceLog(description = "更新一条考勤类型信息失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PunchType punchType) {
        punchTypeMapper.update(punchType);
    }

    /*  通过考勤类型名字查询一条记录   */
    @Override
    public PunchType selectByName(String name) {
        return punchTypeMapper.selectByName(name);
    }

    /*  查询考勤类型表的总记录数    */
    @Override
    public int selectCount() {
        return punchTypeMapper.selectCount();
    }

    /*  通过查询条件查询考勤类型记录    */
    @Override
    public List<PunchType> selectByParam(ConditionMap<PunchType> conditionMap) {
        return punchTypeMapper.selectByParam(conditionMap);
    }
}
