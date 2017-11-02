package com.suny.association.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**************************************
 *  Description  针对JackJson的一些常用方法的封装
 *  @author 孙建荣
 *  @date 17-11-1.下午8:48
 *  @version 1.0
 **************************************/
public class JackJsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JackJsonUtil.class);
        /** 格式化时间的string */
        private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        private static final String JSON_PARSE_EXCEPTION = "解析JSON数据异常";
        private static final String JSON_MAPPING_EXCEPTION = "映射JSON对象到实体类异常";
        private static final String IO_EXCEPTION = "输出输出流异常";
        private static final String JSON_GENERATION_EXCEPTION = "生成JSON数据异常";

        /**
         * jackjson把json字符串转换为Java对象的实现方法
         * @param <T>   转换为的java对象
         * @param json   json字符串
         * @param typeReference    jackjson自定义的类型
         * @return 返回Java对象
         */
        public static <T> T jsonToObject(String json, TypeReference<T> typeReference) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return (T)mapper.readValue(json, typeReference);
            } catch (JsonParseException e) {
                logger.error(JSON_PARSE_EXCEPTION,":{}", e);
            } catch (JsonMappingException e) {
                logger.error(JSON_MAPPING_EXCEPTION,":{}", e);
            } catch (IOException e) {
                logger.error(IO_EXCEPTION,":{}", e);
            }
            return null;
        }


    /**
     *  Json转换成Object对象
     * @param json   json数据
     * @param valueType  转换成的类型
     * @param <T>  泛型限制
     * @return   封装成对应的实体类
     */
    public static <T> T jsonToObject(String json, Class<T> valueType){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, valueType);
        } catch (JsonGenerationException e) {
            logger.error(JSON_GENERATION_EXCEPTION,":{}", e);
        } catch (JsonMappingException e) {
            logger.error(JSON_MAPPING_EXCEPTION,":{}", e);
        } catch (IOException e) {
            logger.error(IO_EXCEPTION,":{}", e);
        }
        return null;
    }


    /**
     *  Object对象转换成json数据
     * @param object  Object对象
     * @return   json类型数据
     */
    @SuppressWarnings("Duplicates")
    public static String objectToJson(Object object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            logger.error(JSON_GENERATION_EXCEPTION,":{}", e);
        } catch (JsonMappingException e) {
            logger.error(JSON_MAPPING_EXCEPTION,":{}", e);
        } catch (IOException e) {
            logger.error(IO_EXCEPTION,":{}", e);
        }
        return null;
    }
        /**
         * java对象(包含日期字段或属性)转换为json字符串
         *
         * @param object
         *            Java对象
         * @return 返回字符串
         */
        @SuppressWarnings("Duplicates")
        public static String objectHasDateToJson(Object object) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializationConfig().with(new SimpleDateFormat(DATE_TIME_FORMAT));
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonGenerationException e) {
                logger.error(JSON_GENERATION_EXCEPTION,":{}", e);
            } catch (JsonMappingException e) {
                logger.error(JSON_MAPPING_EXCEPTION,":{}", e);
            } catch (IOException e) {
                logger.error(IO_EXCEPTION,":{}", e);
            }
            // 过期的方法
            return null;
        }



        /**
         * java对象(包含日期字段或属性)转换为json字符串
         *
         * @param object
         *            Java对象
         * @param dateTimeFormatString
         *            自定义的日期/时间格式。该属性的值遵循java标准的date/time格式规范。如：yyyy-MM-dd
         * @return 返回字符串
         */
        @SuppressWarnings("Duplicates")
        public static String objectHasDateToJson(Object object, String dateTimeFormatString) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializationConfig().with(new SimpleDateFormat(dateTimeFormatString));
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonGenerationException e) {
                logger.error(JSON_GENERATION_EXCEPTION,":{}", e);
            } catch (JsonMappingException e) {
                logger.error(JSON_MAPPING_EXCEPTION,":{}", e);
            } catch (IOException e) {
                logger.error(IO_EXCEPTION,":{}", e);
            }
            return null;
        }


}
