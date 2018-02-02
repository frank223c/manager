package com.suny.association.utils;

import com.suny.association.entity.po.LoginTicket;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 *
 * Created by 孙建荣 on 17-9-24.下午1:30
 */
public class SerializeUtilTest {
    private static Logger logger = LoggerFactory.getLogger(SerializeUtilTest.class);
    private static final String RANDOM_KEY="random_key";

    private JedisAdapter jedisAdapter;

    public void setUp(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath*:Spring/applicationContext.xml");
        jedisAdapter= (JedisAdapter) applicationContext.getBean("jedisAdapter");
    }


    @Test
    public void serialize() throws Exception {

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setAccountId(4);
        loginTicket.setExpired(LocalDateTime.now());
        loginTicket.setStatus(0);
        loginTicket.setTicket(RANDOM_KEY);
        jedisAdapter.set((RedisKeyUtils.getLoginTicketKey(loginTicket.getTicket())).getBytes(), SerializeUtil.serialize(loginTicket));
        byte[] bytes = jedisAdapter.get((RedisKeyUtils.getTicketKey(loginTicket.getTicket())).getBytes());
        if (bytes != null) {
            logger.info(Arrays.toString(bytes));
        } else {
            logger.info("没有取到值");
        }

    }

    @Test
    public void unSerialize() throws Exception {
        byte[] bytes = jedisAdapter.get((RedisKeyUtils.getTicketKey(RANDOM_KEY)).getBytes());
        logger.info(Arrays.toString(bytes));
        Object o = SerializeUtil.unSerialize(bytes);
        if (o != null) {
            LoginTicket loginTicket = (LoginTicket) o;
            logger.info(loginTicket.toString());
        } else {
            logger.info("没有获取到实体信息");
        }
    }

}



































