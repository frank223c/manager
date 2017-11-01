package com.suny.association.async;

import com.suny.association.utils.JackJsonUtil;
import com.suny.association.utils.JedisAdapter;
import com.suny.association.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 事件生产者
 * @author  孙建荣 on 17-9-28.下午5:09
 */
public class EventProducer {

    private final JedisAdapter jedisAdapter;


    @Autowired
    public EventProducer(JedisAdapter jedisAdapter) {
        this.jedisAdapter = jedisAdapter;
    }


    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JackJsonUtil.processObjectToJson(eventModel);
            String key = RedisKeyUtils.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}




























