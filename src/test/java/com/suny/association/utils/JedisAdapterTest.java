package com.suny.association.utils;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * Created by 孙建荣 on 17-9-24.上午10:58
 */
public class JedisAdapterTest {
    private JedisAdapter jedisAdapter;

    @Before
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/Spring/applicationContext.xml");
        jedisAdapter = (JedisAdapter) context.getBean("jedisAdapter");
    }

    @Test
    public void sadd() throws Exception {
    }

    @Test
    public void srem() throws Exception {
    }

    @Test
    public void scard() throws Exception {
    }

    @Test
    public void sismember() throws Exception {
    }

    @Test
    public void set() throws Exception {
        Boolean b = jedisAdapter.set("BIZ_ACCOUNT:12345678", "junior,123");
        System.out.println(b);
    }

    @Test
    public void get() throws Exception {
        String s = jedisAdapter.get("BIZ_ACCOUNT:12345");
        System.out.println(s);
    }

}