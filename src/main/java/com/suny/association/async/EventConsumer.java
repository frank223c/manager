package com.suny.association.async;

import com.suny.association.async.ThreadPool.CustomerThreadPoolManager;
import com.suny.association.utils.JackJsonUtil;
import com.suny.association.utils.JedisAdapter;
import com.suny.association.utils.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 事件消费者，主要是从Redis里面不停地读取队列中的事件进行处理
 *
 * @author 孙建荣
 *         on 17-9-28.下午5:47
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    private final JedisAdapter jedisAdapter;

    @Autowired
    public EventConsumer(JedisAdapter jedisAdapter) {
        this.jedisAdapter = jedisAdapter;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // 载入所有类型为EventHandler的bean
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        CustomerThreadPoolManager.getInstance().execute(() -> {
            // 一直循环线程
            while (true) {
                // 获取一个放在Redis里面事件类型的key
                String key = RedisKeyUtils.getEventQueueKey();
                // 弹出Redis里面的一堆事件，为JSON字符串
                List<String> events = jedisAdapter.brpop(0, key);
                for (String message : events) {
                    if (message.equals(key)) {
                        continue;
                    }
                    // 把JSON转成一个事件模型实体类
                    EventModel eventModel = JackJsonUtil.jsonToObject(message, EventModel.class);
                    // 如果没有这种事件的话就跳过不处理
                    if (!config.containsKey(eventModel != null ? eventModel.getType() : null)) {
                        logger.error("不能识别的事件");
                        continue;
                    }
                    // 循环处理事件
                    for (EventHandler handler : config.get(eventModel.getType())) {
                        handler.doHandle(eventModel);
                    }
                }
            }
        });
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}





















