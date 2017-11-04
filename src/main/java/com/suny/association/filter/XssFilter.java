package com.suny.association.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**************************************
 *  Description  过滤XSS攻击过滤器
 *  @author 孙建荣
 *  @date 17-11-2.下午10:00
 *  @version 1.0
 **************************************/
public class XssFilter implements Filter{
    private static Logger logger = LoggerFactory.getLogger(XssFilter.class);
     private FilterConfig filterConfig=null;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig=filterConfig;
       logger.info("XSS攻击过滤器开始初始化");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        XssRequestWrapper xssRequestWrapper = new XssRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequestWrapper,response);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
        logger.info("XSS攻击过滤器销毁");
    }


}
