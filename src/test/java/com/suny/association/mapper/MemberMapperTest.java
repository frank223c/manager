package com.suny.association.mapper;

import com.suny.association.pojo.po.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 17-10-24.下午8:26
 *  @version 1.0
 **************************************/
public class MemberMapperTest {
    private MemberMapper memberMapper;
    private static Logger logger = LoggerFactory.getLogger(MemberMapperTest.class);

    @Before
    public void setBefore() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:Spring/applicationContext.xml");
        memberMapper = (MemberMapper) applicationContext.getBean("memberMapper");
    }


    @Test
    public void selectFreezeManager() throws Exception {
    }

    @Test
    public void selectNormalManager() throws Exception {
    }

    @Test
    public void selectFreezeMember() throws Exception {
    }

    @Test
    public void selectNormalMember() throws Exception {
    }

    @Test
    public void selectByMemberRoleId() throws Exception {
    }

    @Test
    public void selectLimitMemberRole() throws Exception {
    }

}