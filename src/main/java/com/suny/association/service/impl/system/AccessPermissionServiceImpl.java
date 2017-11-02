package com.suny.association.service.impl.system;

import com.suny.association.mapper.AccessPermissionMapper;
import com.suny.association.entity.po.AccessPermission;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.interfaces.system.IAccessPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comments:   访问url地址所需要的权限业务逻辑层
 * @author :   孙建荣
 * Create Date: 2017/05/12 22:13
 */
@Service
public class AccessPermissionServiceImpl implements IAccessPermissionService {

    private final AccessPermissionMapper accessPermissionMapper;

    @Autowired
    public AccessPermissionServiceImpl(AccessPermissionMapper accessPermissionMapper) {
        this.accessPermissionMapper = accessPermissionMapper;
    }

    @Override
    public void insert(AccessPermission accessPermission) {

    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void update(AccessPermission accessPermission) {

    }

    @Override
    public AccessPermission selectById(long id) {
        return null;
    }

    @Override
    public AccessPermission selectByName(String name) {
        return accessPermissionMapper.selectByName(name);
    }

    @Override
    public int selectCount() {
        return 0;
    }

    @Override
    public List<AccessPermission> selectAll() {
        return accessPermissionMapper.selectAll();
    }

    @Override
    public List<AccessPermission> selectByParam(ConditionMap<AccessPermission> conditionMap) {
        return accessPermissionMapper.selectByParam(conditionMap);
    }
}
