package com.suny.association.service.impl.system;

import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.mapper.PermissionMapper;
import com.suny.association.pojo.po.Department;
import com.suny.association.pojo.po.Permission;
import com.suny.association.pojo.po.PermissionAllot;
import com.suny.association.pojo.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.system.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Comments:   权限管理业务逻辑控制
 * @author :   孙建荣
 * Create Date: 2017/05/02 13:04
 */
@Service
public class PermissionServiceImpl extends AbstractBaseServiceImpl<Permission> implements IPermissionService {

    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @SystemServiceLog(description = "插入权限失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(Permission permission) {
        permissionMapper.insert(permission);
    }

    @Transactional(rollbackFor = Exception.class)
    @SystemServiceLog(description = "删除权限失败")
    @Override
    public void deleteById(long id) {
        permissionMapper.deleteById(id);
    }

    @SystemServiceLog(description = "更新权限失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Permission permission) {
        permissionMapper.update(permission);
    }


    @Override
    public Permission selectByName(String name) {
        return permissionMapper.selectByName(name);
    }

    @Override
    public int selectCount() {
        return permissionMapper.selectCount();
    }

    @Override
    public Permission selectById(long id) {
        return permissionMapper.selectById(id);
    }


    @Override
    public List<Permission> selectByParam(ConditionMap<Permission> conditionMap) {
        return permissionMapper.selectByParam(conditionMap);
    }

    @Override
    public List<PermissionAllot> queryPermissionQuote(int permissionId) {
        return permissionMapper.queryPermissionQuote(permissionId);
    }

    @Override
    public List<Permission> selectAll() {
        return permissionMapper.selectAll();
    }
}
