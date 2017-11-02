package com.suny.association.service.impl.core;

import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.mapper.MemberMapper;
import com.suny.association.mapper.MemberRolesMapper;
import com.suny.association.entity.po.Member;
import com.suny.association.entity.po.MemberRoles;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.core.IMemberRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Comments:  协会角色操作
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:36
 */
@Service
public class MemberRolesServiceImpl extends AbstractBaseServiceImpl<MemberRoles> implements IMemberRolesService {
    private  MemberMapper memberMapper;
    private MemberRolesMapper memberRolesMapper;

    @Autowired
    public MemberRolesServiceImpl(MemberRolesMapper memberRolesMapper, MemberMapper memberMapper) {
        this.memberRolesMapper = memberRolesMapper;
        this.memberMapper = memberMapper;
    }


    /*    通过成员角色的id删除一条成员角色记录  */
    @SystemServiceLog(description = "删除一条成员角色记录失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(long memberRoleId) {
        memberRolesMapper.deleteById(memberRoleId);
    }

    /*  通过成员的名字查询成员角色    */
    @Override
    public MemberRoles selectByName(String name) {
        return memberRolesMapper.selectByName(name);
    }

    /*  查询数据库中成员角色表的总记录数    */
    @Override
    public int selectCount() {
        return memberRolesMapper.selectCount();
    }

    /*  通过成员角色id查询一个成员角色    */
    @Override
    public MemberRoles selectById(long id) {
        return memberRolesMapper.selectById(id);
    }

    /**
     * 查询所有的成员角色列表
     *
     * @return 所有的成员角色数据
     */
    @Override
    public List<MemberRoles> selectAll() {
        return memberRolesMapper.selectAll();
    }

    /**
     * 根据查询条件查询成员角色
     *
     * @param conditionMap 封装的查询条件
     * @return 成员角色
     */
    @Override
    public List<MemberRoles> selectByParam(ConditionMap<MemberRoles> conditionMap) {
        return memberRolesMapper.selectByParam(conditionMap);
    }

    /**
     * 插入一条成员角色记录
     *
     * @param memberRoles 成员角色信息
     */
    @SystemServiceLog(description = "插入一条成员角色记录失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(MemberRoles memberRoles) {
        memberRolesMapper.insert(memberRoles);
    }

    /**
     * 通过成员角色id查询引用该角色的成员
     *
     * @param memberRoleId 成员角色id
     * @return 引用该角色的成员
     */
    @Override
    public List<Member> selectByMemberRoleId(Integer memberRoleId) {
        return memberMapper.selectByMemberRoleId(memberRoleId);
    }


    /**
     * 更新成员角色信息
     *
     * @param memberRoles 成员角色信息
     */
    @SystemServiceLog(description = "更新一条成员角色记录失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(MemberRoles memberRoles) {
        memberRolesMapper.update(memberRoles);
    }
}
