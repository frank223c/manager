package com.suny.association.service.impl;


import com.suny.association.mapper.AccountMapper;
import com.suny.association.pojo.po.Account;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountServiceImplTest {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImplTest.class);
    private AccountMapper accountMapper;


    @Before
    public void setUp(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:Spring/applicationContext.xml");
        accountMapper= (AccountMapper) applicationContext.getBean("accountMapper");
    }

    @Test
    public void testSelect() {
        Account account = accountMapper.selectById(1);
        logger.info(account.toString());
        logger.info("你好");
    }


}