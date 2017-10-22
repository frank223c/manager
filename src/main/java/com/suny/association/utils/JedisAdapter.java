package com.suny.association.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 操作Redis工具类
 * Created by 孙建荣 on 17-9-24.上午8:54
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool jedisPool;


    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("redis://localhost:6379/10");
    }


    /**
     * 往集合里面添加元素.
     *
     * @param key   key
     * @param value value
     * @return 添加成功就返回非0，失败返回0
     */
    public long sadd(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("往集合里面添加元素发生了异常" + e.getMessage());
        }
        return 0;
    }

    /**
     * 往集合里面删除元素.
     *
     * @param key   key
     * @param value value
     * @return 删除成功就返回非0，失败返回0
     */
    public long srem(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("删除集合里面的元素发生了异常" + e.getMessage());
        }
        return 0;
    }


    /**
     * 查看集合里面的元素数量.
     *
     * @param key key
     * @return 出现错误返回0，否则就返回查询到的数量
     */
    public long scard(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("查看集合里面的元素数量发生了异常" + e.getMessage());
        }
        return 0;
    }

    /**
     * 判断是否在set里面.
     *
     * @param key   key
     * @param value value
     * @return 1就表示存在，0就表示不存在
     */
    public Boolean sismember(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("查询集合里面是否有元素发生了异常" + e.getMessage());
        }
        return false;
    }


    public Boolean set(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            String s = jedis.set(key, value);
            return "OK".equals(s);
        } catch (Exception e) {
            logger.error("加入元素发生了异常" + e.getMessage());
        }
        return false;
    }

    /**
     * 添加一个字符串key
     *
     * @param key   key
     * @param value value
     * @return 成功就返回true，失败返回false
     */
    public Boolean set(byte[] key, byte[] value) {
        try (Jedis jedis = jedisPool.getResource()) {
            String s = jedis.set(key, value);
            return "OK".equals(s);
        } catch (Exception e) {
            logger.error("加入元素发生了异常" + e.getMessage());
        }
        return false;
    }

    /**
     * 查询数据库中的某个key值
     *
     * @param key key
     * @return 有就返回对应的值，没有就返回null
     */
    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("获取元素数量发生了异常" + e.getMessage());
        }
        return null;
    }

    /**
     * 查询数据库中的某个key值
     *
     * @param key key
     * @return 有就返回对应的值，没有就返回null
     */
    public byte[] get(byte[] key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("获取元素数量发生了异常" + e.getMessage());
        }
        return null;
    }


    /**
     * 给key设置过期时间
     *
     * @param key     key
     * @param seconds 过期的秒数
     * @return 设置成功返回1，失败返回0
     */
    public long setExpire(String key, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("设置过期时间发生了异常" + e.getMessage());
        }
        return 0;
    }

    /**
     * 查看某个key的过期时间
     *
     * @param key key
     * @return 剩余过期的秒数
     */
    public long getExpireTime(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.ttl(key);
        } catch (Exception e) {
            logger.error("查看过期时间发生了异常" + e.getMessage());
        }
        return 0;
    }


    public long lpush(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生了异常" + e.getMessage());
        }
        return 0;
    }


    public List<String> brpop(int timeout, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.brpop(key);
        } catch (Exception e) {
            logger.error("发生了异常" + e.getMessage());
        }
        return null;
    }


}





























