package com.suny.association.filter;

import com.suny.association.utils.EscapeXssUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 17-11-2.下午10:13
 *  @version 1.0
 **************************************/
public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String parameter) {
        // 返回值之前 先进行过滤
        return EscapeXssUtil.escapeXss(super.getParameter(parameter));
    }


}
