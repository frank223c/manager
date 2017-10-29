package com.suny.association.service.impl.core;

import com.suny.association.pojo.po.Member;
import com.suny.association.service.interfaces.core.IMemberService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 17-10-26.下午9:46
 *  @version 1.0
 **************************************/
public class MemberServiceImplTest {
    private static Logger logger = LoggerFactory.getLogger(MemberServiceImplTest.class);
    private IMemberService memberServiceImpl;

    @Before
    public void setUp() throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:Spring/applicationContext.xml");
        memberServiceImpl = (IMemberService) applicationContext.getBean("memberServiceImpl");
    }

    @Test
    public void insert() throws Exception {
        Member member = new Member();
        member.setMemberName("测试成员姓名");
        member.setMemberId(10000);
        member.setMemberGradeNumber(2017);
        member.setMemberClassName("测试班级");
        memberServiceImpl.insert(member);
    }

    @Test
    public void selectMemberReference() throws Exception {
        memberServiceImpl.selectMemberReference(1);
    }


    @Test
    public void insertReturnCount() throws Exception {
    }

    @Test
    public void deleteById() throws Exception {
    }

    @Test
    public void update() throws Exception {
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
    public void selectByMemberRoleId() throws Exception {
    }

    @Test
    public void insertBatchFormFile() throws Exception {
    }

    @Test
    public void insertBatch() throws Exception {
    }

    @Test
    public void insertBatch1() throws Exception {
    }

    @Test
    public void selectNormalMember() throws Exception {
    }

    @Test
    public void selectById() throws Exception {
    }

    @Test
    public void selectByName() throws Exception {
    }

    @Test
    public void selectAll() throws Exception {
    }

    @Test
    public void selectByParam() throws Exception {
    }

}