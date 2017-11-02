package com.suny.association.mapper;

import com.suny.association.entity.po.MemberRoles;
import com.suny.association.entity.vo.ConditionMap;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 17-11-2.下午4:41
 *  @version 1.0
 **************************************/
public class MemberRolesMapperTest {
    private static Logger logger = LoggerFactory.getLogger(MemberRolesMapperTest.class);
    private MemberRolesMapper memberRolesMapper;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:Spring/applicationContext.xml");
        logger.info("{}",applicationContext.getBean("memberMapper").toString());
        memberRolesMapper = (MemberRolesMapper) applicationContext.getBean("memberRolesMapper");
    }

    @Test
    public void insert(){
        MemberRoles memberRoles=new MemberRoles(1011,"测试角色");
        memberRolesMapper.insert(memberRoles);
    }

    @Test
    public void deleteById(){
        memberRolesMapper.deleteById(16);
    }

    @Test
    public void selectById(){
        MemberRoles memberRoles = memberRolesMapper.selectById(15);
        logger.info(memberRoles.toString());
    }

    @Test
    public void selectByName(){
        MemberRoles memberRoles = memberRolesMapper.selectByName("干事");
        logger.info(memberRoles.toString());
    }

    @Test
    public void selectCount(){
        int count = memberRolesMapper.selectCount();
        logger.info("{}",count);
    }



    @Test
    public void selectAll(){
        List<MemberRoles> memberRolesList = memberRolesMapper.selectAll();
        for (MemberRoles memberRoles : memberRolesList) {
              logger.info(memberRoles.toString());
        }
    }

    @Test
    public void update(){
        MemberRoles memberRoles=new MemberRoles();
        memberRoles.setMemberRoleId(15);
        memberRoles.setMemberRoleName("干事1111111111");
        memberRolesMapper.update(memberRoles);
    }

    @Test
    public void selectByParam(){
        MemberRoles memberRoles=new MemberRoles();
        memberRoles.setMemberRoleId(0);
        memberRoles.setMemberRoleName("干事");
        ConditionMap<MemberRoles> conditionMap=new ConditionMap<>(memberRoles,0,10);
        List<MemberRoles> memberRolesList = memberRolesMapper.selectByParam(conditionMap);
        for (MemberRoles roles : memberRolesList) {
            logger.info(roles.toString());
        }
    }

    @Test
    public void queryQuote() throws Exception {
    }

}