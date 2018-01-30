package com.suny.association.service.impl;

import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.entity.po.AccountRoles;
import com.suny.association.mapper.RolesMapper;
import com.suny.association.entity.po.Account;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Comments:   账号角色业务逻辑
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:41
 */
@Service
public class RolesServiceImpl extends AbstractBaseServiceImpl<AccountRoles> implements IRolesService {

    private final RolesMapper rolesMapper;

    @Autowired
    public RolesServiceImpl(RolesMapper rolesMapper) {
        this.rolesMapper = rolesMapper;
    }

    /* 通过账号角色id删除一条账号角色id  */
    @SystemServiceLog(description = "删除账号角色信息失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(long id) {
        rolesMapper.deleteById(id);
    }

    /*  更新一条账号角色信息 */
    @SystemServiceLog(description = " 更新账号角色失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(AccountRoles accountRoles) {
        if (accountRoles.getRoleId() == null) {
            throw new RuntimeException();
        }
        rolesMapper.update(accountRoles);
    }

    /*  插入一条账号角色 */
    @SystemServiceLog(description = "插入账号角色失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(AccountRoles accountRoles) {
        rolesMapper.insert(accountRoles);
    }

    /* 查询所有的账号角色     */
    @Override
    public List<AccountRoles> selectAll() {
        return rolesMapper.selectAll();
    }

    /* 通过查询条件查询账号角色     */
    @Override
    public List<AccountRoles> selectByParam(ConditionMap<AccountRoles> conditionMap) {
        return rolesMapper.selectByParam(conditionMap);
    }

    /* 通过账号角色id查询账号角色     */
    @Override
    public AccountRoles selectById(long id) {
        return rolesMapper.selectById(id);
    }

    /* 通过账号角色表中的记录数     */
    @Override
    public int selectCount() {
        return rolesMapper.selectCount();
    }

    /**
     * 根据查询条件查询操作记录
     *
     * @param accountRoles 操作日志实体
     * @return 条件查询出来的账号
     */
    @Override
    public int selectCountByParam(AccountRoles accountRoles) {
        return rolesMapper.selectCountByParam(accountRoles);
    }

    /* 查询触引用该角色的所有账号信息     */
    @Override
    public List<Account> queryQuote(Integer roleId) {
        return rolesMapper.queryQuote(roleId);
    }

    /* 通过角色名字查询账号角色     */
    @Override
    public AccountRoles selectByName(String name) {
        return rolesMapper.selectByName(name);
    }
}
