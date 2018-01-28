package com.suny.association.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Comments:    基础的控制器
 *
 * @author :   孙建荣
 *         Create Date: 2017/05/06 22:23
 */
public abstract class BaseController {
    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

   /* *//**
     * 输出json数据
     *
     * @param map      封装数据集合
     * @param response 响应
     *//*
    private void writeJson(Map<String, Object> map, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter printWriter = response.getWriter()) {
            assert printWriter != null;
            printWriter.write(JackJsonUtil.objectToJson(map));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


    }*/
}













