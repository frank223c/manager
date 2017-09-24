package com.suny.association.utils;

import com.suny.association.pojo.po.LoginTicket;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by 孙建荣 on 17-9-24.下午1:30
 */
public class SerializeUtilTest {

    JedisAdapter jedisAdapter = new JedisAdapter();


    @Test
    public void serialize() throws Exception {

        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setAccountId(4);
        loginTicket.setExpired(LocalDateTime.now());
        loginTicket.setStatus(0);
        loginTicket.setTicket("dsfdsafdsfds");
        jedisAdapter.set("loginTicket".getBytes(), SerializeUtil.serialize(loginTicket));
        byte[] bytes = jedisAdapter.get("loginTicket".getBytes());
        System.out.println(bytes.toString());

    }

    @Test
    public void unserialize() throws Exception {
        byte[] bytes = jedisAdapter.get("loginTicket".getBytes());
        System.out.println(bytes.toString());
        Object o = SerializeUtil.unserialize(bytes);
        if (o != null) {
            LoginTicket loginTicket = (LoginTicket) o;
            System.out.println(loginTicket.toString());
        } else {
            System.out.println("没有获取到实体信息");
        }
    }

}



































