package com.suny.association.mapper;

import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.Member;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 17-10-29.下午3:53
 *  @version 1.0
 **************************************/
public class AccountMapperTest {
    private static Logger logger = LoggerFactory.getLogger(AccountMapperTest.class);
    private AccountMapper accountMapper;

    @Before
    public void setUp() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:Spring/applicationContext.xml");
        accountMapper = (AccountMapper) applicationContext.getBean("accountMapper");
    }

    @Test
    public void selectMemberReference() throws Exception {
        Account account = accountMapper.selectMemberReference(1);
        logger.info(account.toString());
    }

    @Test
    public void insertBatchSimpleAccount() {

        List<Account> accountList = new ArrayList<>();

        Account account = new Account();
        account.setAccountName("testBatch");
        Member member = new Member();
        member.setMemberId(10419);
        account.setAccountMember(member);

        Account account2 = new Account();
        account2.setAccountName("testBatch");
        Member member2 = new Member();
        member2.setMemberId(10420);
        account2.setAccountMember(member);

        accountList.add(account);
        accountList.add(account2);
        int i = accountMapper.insertBatchSimpleAccount(accountList);
        logger.info("{}", i);
    }

    @Test
    public void queryByPhone() throws Exception {
    }

    @Test
    public void queryByMail() throws Exception {
    }

    @Test
    public void queryQuoteByAccountId() throws Exception {
    }

    @Test
    public void queryQuoteByMemberId() throws Exception {
    }

    @Test
    public void queryByMemberId() throws Exception {
    }

    @Test
    public void changePassword() throws Exception {
    }

}