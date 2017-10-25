package com.suny.association.mapper;

import com.suny.association.pojo.po.Department;
import com.suny.association.pojo.po.Member;
import com.suny.association.pojo.po.MemberRoles;
import com.suny.association.pojo.vo.ConditionMap;
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
    public void selectByParam() {
        Member memberVO = new Member();
        memberVO.setMemberId(10000);
        ConditionMap<Member> conditionMap = new ConditionMap(memberVO, 0, 10);
        List<Member> memberList = memberMapper.selectByParam(conditionMap);
        for (Member member : memberList) {
            logger.info(member.toString());
        }
    }

    @Test
    public void selectAll() throws Exception {
        List<Member> memberList = memberMapper.selectAll();
        for (Member member : memberList) {
            logger.info(member.toString());
        }
    }

    @Test
    public void selectById() throws Exception {
        Member member = memberMapper.selectById(10000);
        logger.info(member.toString());
    }

    @Test
    public void update() throws Exception {
        Member member = new Member();
        member.setMemberId(10003);
        member.setMemberClassName("机电3班");
        memberMapper.update(member);
    }

    @Test
    public void deleteById() throws Exception {
        memberMapper.deleteById(10416);
    }

    @Test
    public void insert() throws Exception {
        Member member = new Member();
        member.setMemberName("测试姓名");
        member.setMemberClassName("测试班级");
        member.setMemberStatus(true);
        member.setMemberDepartment(new Department(1, "办公室"));
        member.setMemberGradeNumber(2017);
        member.setMemberRoles(new MemberRoles(0, "干事"));
        Member manager = new Member();
        manager.setMemberId(10000);
        member.setMemberManager(manager);
        memberMapper.insert(member);
    }

    @Test
    public void insertAndReturnId() throws Exception {
        Member member = new Member();
        member.setMemberName("测试姓名2");
        member.setMemberClassName("测试班级2");
        member.setMemberStatus(true);
        member.setMemberDepartment(new Department(1, "办公室"));
        member.setMemberGradeNumber(2017);
        member.setMemberRoles(new MemberRoles(0, "干事"));
        Member manager = new Member();
        manager.setMemberId(10000);
        member.setMemberManager(manager);
        memberMapper.insertAndReturnId(member);
    }

    @Test
    public void selectCount() {
        int count = memberMapper.selectCount();
        logger.info("数据库中总的数量为{}", count);
    }


    @Test
    public void selectFreezeManager() throws Exception {
        List<Member> memberList = memberMapper.selectFreezeManager();
        for (Member member : memberList) {
            logger.info(member.toString());
        }
    }

    @Test
    public void selectNormalManager() throws Exception {
        List<Member> memberList = memberMapper.selectNormalManager();
        for (Member member : memberList) {
            logger.info(member.toString());
        }
    }

    @Test
    public void selectFreezeMember() throws Exception {
        List<Member> memberList = memberMapper.selectFreezeMember();
        for (Member member : memberList) {
            logger.info(member.toString());
        }
    }

    @Test
    public void selectNormalMember() throws Exception {
        List<Member> memberList = memberMapper.selectNormalMember();
        for (Member member : memberList) {
            logger.info(member.toString());
        }
    }

    @Test
    public void selectByMemberRoleId() throws Exception {
        List<Member> memberList = memberMapper.selectByMemberRoleId(3);
        for (Member member : memberList) {
            logger.info(member.toString());
        }

    }

    @Test
    public void selectLimitMemberRole() throws Exception {
        List<Member> memberList = memberMapper.selectFreezeMemberByParam(0, 2016);
        for (Member member : memberList) {
            logger.info(member.toString());
        }
    }

}