package com.suny.association.service.impl;


import com.suny.association.mapper.AccountMapper;
import com.suny.association.pojo.po.Account;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountServiceImplTest {
    private AccountMapper accountMapper;


    @Before
    public void setUp(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:Spring/applicationContext.xml");
        accountMapper= (AccountMapper) applicationContext.getBean("accountMapper");
    }

    @Test
    public void testSelect() {
        Account account = accountMapper.selectById(1);
        System.out.println(account.toString());
        System.out.println("你好");
    }


}