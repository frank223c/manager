package com.suny.association.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
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

        /**
         * jackjson把json字符串转换为Java对象的实现方法
         * @param <T>
         *            转换为的java对象
         * @param json
         *            json字符串
         * @param typeReference
         *            jackjson自定义的类型
         * @return 返回Java对象
         */
        public static <T> T processJsonToObject(String json, TypeReference<T> typeReference) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return (T)mapper.readValue(json, typeReference);
            } catch (JsonParseException e) {
                logger.error("JsonParseException: ", e);
            } catch (JsonMappingException e) {
                logger.error("JsonMappingException: ", e);
            } catch (IOException e) {
                logger.error("IOException: ", e);
            }
            return null;
        }



    public static <T> T processJsonToObject(String json, Class<T> valueType){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, valueType);
        } catch (JsonGenerationException e) {
            logger.error("JsonGenerationException: ", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException: ", e);
        } catch (IOException e) {
            logger.error("IOException: ", e);
        }
        return null;
    }



    public static String processObjectToJson(Object object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            logger.error("JsonGenerationException: ", e);
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException: ", e);
        } catch (IOException e) {
            logger.error("IOException: ", e);
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
        public static String processObjectHasDateToJson(Object object) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializationConfig().with(new SimpleDateFormat(DATE_TIME_FORMAT));
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonGenerationException e) {
                logger.error("JsonGenerationException: ", e);
            } catch (JsonMappingException e) {
                logger.error("JsonMappingException: ", e);
            } catch (IOException e) {
                logger.error("IOException: ", e);
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
        public static String processObjectHasDateToJson(Object object, String dateTimeFormatString) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.getSerializationConfig().with(new SimpleDateFormat(dateTimeFormatString));
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonGenerationException e) {
                logger.error("JsonGenerationException: ", e);
            } catch (JsonMappingException e) {
                logger.error("JsonMappingException: ", e);
            } catch (IOException e) {
                logger.error("IOException: ", e);
            }
            return null;
        }


        public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        }
}
