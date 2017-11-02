package com.suny.association.service.impl.core;

import com.suny.association.mapper.DepartmentMapper;
import com.suny.association.entity.po.Department;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.core.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comments:   部门表业务逻辑
 * @author :   孙建荣
 * Create Date: 2017/03/17 22:34
 */
@Service
public class DepartmentServiceImpl extends AbstractBaseServiceImpl<Department> implements IDepartmentService {
    private DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    public DepartmentServiceImpl() {
    }

    /* 通过部门名字查询部门记录  */
    @Override
    public Department selectByName(String name) {
        return departmentMapper.selectByName(name);
    }

    /* 查询部门表总记录数  */
    @Override
    public int selectCount() {
        return departmentMapper.selectCount();
    }

    /* 查询部门表所有的记录  */
    @Override
    public List<Department> selectAll() {
        return departmentMapper.selectAll();
    }

    /**
     * 通过查询条件查询部门表记录
     *
     * @param conditionMap 自己封装的查询条件
     * @return 带查询条件的部门表记录
     */
    @Override
    public List<Department> selectByParam(ConditionMap<Department> conditionMap) {
        return departmentMapper.selectByParam(conditionMap);
    }


}
