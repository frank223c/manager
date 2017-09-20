package com.suny.association.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 基本访问URL操作,当用户访问项目第一个进入的过滤器.
 * Created by 孙建荣 on 17-9-20.下午1:25
 */
public class LimitFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(LimitFilter.class);
    private static final String EXECUTE_NEXT_FILTER = "EXECUTE_NEXT_FILTER";

    /*   不需要登录就可以访问的资源*/
    private static final String[] INHERENT_ESCAPE_URIS = {
            "/", "index.jsp", "/base/login.html", "/403.jsp", "/code/generateCode.action", "/code/checkCode.action", "/base/login.action"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("===============URL验证过滤器开始初始化============");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String reqURI = req.getRequestURI();
        // 1. 项目中的第一个Filter,如果是一些生成验证码或者是请求登录,验证账号信息等请求的话直接放行
        if (isExcludeUrl(reqURI, req)) {
            //  1.1     如果是被排除的URL的话就直接放行,不要阻拦了,直接跳过剩下的Filter
            //  1.1.1   在每个Filter中定义一个标记,然后放入ServletRequest中,在别的Filter中取出标记进行判断是否跳过
            logger.info("请求的地址为{},直接放行到Servlet,不再经过Filter", reqURI);
            req.setAttribute(EXECUTE_NEXT_FILTER, false);
        }
        // 2. 放行到登录下一个登录验证过滤器
        logger.info("请求的url{}地址是否需要进行下一步认证{}", reqURI, req.getAttribute(EXECUTE_NEXT_FILTER));
        chain.doFilter(request, response);
    }

    /**
     * 判断当前访问的url地址是否为可以直接放行的地址.
     *
     * @param requestURI 请求的url地址,不包含项目路径.
     * @param request    请求相关数据
     * @return 是直接放行的地址就返回true, 否则就返回false
     */
    private boolean isExcludeUrl(String requestURI, HttpServletRequest request) {
        //  1. 把请求的url忽略大小写,如果是访问主页的话直接放行
        if (request.getContextPath().equalsIgnoreCase(requestURI) || (request.getContextPath() + "/").equalsIgnoreCase(requestURI)) {
            return true;
        } else {
            //  2.   循环匹配是否是直接放行的url地址,如果匹配成功的话就返回
            for (String uri : INHERENT_ESCAPE_URIS) {
                if (requestURI != null && uri.contains(requestURI)) {
                    return true;
                }
            }
            return false;
        }
    }


    @Override
    public void destroy() {

    }
}
