package com.suny.association.service.impl.core;

import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.mapper.ApplicationMessageMapper;
import com.suny.association.entity.po.ApplicationMessage;
import com.suny.association.entity.po.CallbackResult;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.core.IApplicationMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Comments:   考勤记录异议申请业务逻辑
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:30
 */
@Service
public class ApplicationMessageServiceImpl extends AbstractBaseServiceImpl<ApplicationMessage> implements IApplicationMessageService {

    private ApplicationMessageMapper applicationMessageMapper;

    @Autowired
    public ApplicationMessageServiceImpl(ApplicationMessageMapper applicationMessageMapper) {
        this.applicationMessageMapper = applicationMessageMapper;
    }

    public ApplicationMessageServiceImpl() {
    }


    /**
     * 插入一条异议考勤记录
     *
     * @param applicationMessage 申请信息
     */
    @SystemServiceLog(description = "插入异议考勤记录申请失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(ApplicationMessage applicationMessage) {
        applicationMessageMapper.insert(applicationMessage);
    }

    /**
     * 通过申请记录id查询申请记录
     *
     * @param id 申请的id
     * @return 通过id查询出来的记录
     */
    @Override
    public ApplicationMessage selectById(long id) {
        return applicationMessageMapper.selectById(id);
    }

    /**
     * 通过用户名进行查询
     *
     * @param name 用户名
     * @return 通过用户名查询出来的结果
     */
    @Override
    public ApplicationMessage selectByName(String name) {
        return applicationMessageMapper.selectByName(name);
    }

    /**
     * 查询申请表里面的记录条数
     *
     * @return 总记录数
     */
    @Override
    public int selectCount() {
        return applicationMessageMapper.selectCount();
    }

    /**
     * 条件查询异议考勤记录
     *
     * @param conditionMap 封装查询条件
     * @return 带查询条件的记录
     */
    @Override
    public List<ApplicationMessage> selectByParam(ConditionMap<ApplicationMessage> conditionMap) {
        return applicationMessageMapper.selectByParam(conditionMap);
    }


    /**
     * 更新异议考勤申请数据中的结果
     *
     * @param applicationMessage 要更新的一条数据
     * @param callbackResult     审批结果
     */
    @SystemServiceLog(description = "更新异议考勤申请结果失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateApplyForResult(ApplicationMessage applicationMessage, CallbackResult callbackResult) {
        applicationMessage.setApplicationResult(callbackResult);
        this.update(applicationMessage);
    }

    /**
     * 更新一条异议考勤申请记录
     *
     * @param applicationMessage 异议考勤信息
     */
    @SystemServiceLog(description = "更新异议考勤申请记录失败")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void update(ApplicationMessage applicationMessage) {
        applicationMessageMapper.update(applicationMessage);
    }
}
