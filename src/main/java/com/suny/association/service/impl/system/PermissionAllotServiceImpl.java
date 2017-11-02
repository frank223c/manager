package com.suny.association.service.impl.system;

import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.mapper.PermissionAllotMapper;
import com.suny.association.entity.po.PermissionAllot;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.system.IPermissionAllotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Comments:   权限分配业务逻辑实现类
 *
 * @@author :   孙建荣
 * Create Date: 2017/05/02 13:10
 */
@Service
public class PermissionAllotServiceImpl extends AbstractBaseServiceImpl<PermissionAllot> implements IPermissionAllotService {
    private final PermissionAllotMapper permissionAllotMapper;

    @Autowired
    public PermissionAllotServiceImpl(PermissionAllotMapper permissionAllotMapper) {
        this.permissionAllotMapper = permissionAllotMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @SystemServiceLog(description = "增加角色权限失败")
    @Override
    public void insert(PermissionAllot permissionAllot) {
        permissionAllotMapper.insert(permissionAllot);
    }


    @Transactional(rollbackFor = Exception.class)
    @SystemServiceLog(description = "删除角色所有权限失败")
    @Override
    public void deleteById(long id) {
        permissionAllotMapper.deleteById(id);
    }

    @Override
    public PermissionAllot selectByName(String name) {
        return permissionAllotMapper.selectByName(name);
    }

    @Override
    public int selectCount() {
        return permissionAllotMapper.selectCount();
    }

    @Override
    public List<PermissionAllot> selectByParam(ConditionMap<PermissionAllot> conditionMap) {
        return permissionAllotMapper.selectByParam(conditionMap);
    }

    @Override
    public List<PermissionAllot> selectAll() {
        return permissionAllotMapper.selectAll();
    }

    @Override
    public List<PermissionAllot> queryByRoleId(int roleId) {
        return permissionAllotMapper.queryByRoleId(roleId);
    }
}
