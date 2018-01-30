package com.suny.association.cache;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**************************************
 *  Description  缓存管理类
 *  @author 孙建荣
 *  @date 18-1-29.下午3:35
 *  @version 1.0
 **************************************/
public class CacheManager {
    private static HashMap<String, Cache> cacheMap = new HashMap();

    public CacheManager() {
        super();
    }


    /**
     * 取到缓存
     *
     * @param key 要取值的key
     * @return Cache对象
     */
    public static synchronized Cache getCache(String key) {
        return cacheMap.get(key);
    }

    /**
     * 缓存里面的数据
     */
    public static synchronized void clearAll() {
        cacheMap.clear();
    }

    /**
     * 清楚所有带type前缀的数据
     *
     * @param type 类型前缀
     */
    public static synchronized void clearAllType(String type) {
        Iterator iterator = cacheMap.entrySet().iterator();
        String key;
        ArrayList<String> list = new ArrayList<>();
        try {
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                key = (String) entry.getKey();
                // 把前缀为该key的类型加入到list
                if (key.startsWith(type)) {
                    list.add(key);
                }
            }
            for (String aList : list) {
                clearOnly(aList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除指定的缓存
     *
     * @param key key
     */
    public static synchronized void clearOnly(String key) {
        cacheMap.remove(key);
    }

    /**
     * 把数据放入缓存
     *
     * @param key         key的名字
     * @param cacheObject 缓存对象
     */
    public static void putCache(String key, Cache cacheObject) {
        cacheMap.put(key, cacheObject);
    }

    public static int getCacheSize() {
        return cacheMap.size();
    }
}




















