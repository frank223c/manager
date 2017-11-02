package com.suny.association.filter;

import com.suny.association.mapper.AccountMapper;
import com.suny.association.mapper.LoginTicketMapper;
import com.suny.association.entity.po.LoginTicket;
import com.suny.association.utils.LoginTicketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author 孙建荣
 * @date 17-9-20
 */
public class RequireLoginFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(RequireLoginFilter.class);
    private static final String EXECUTE_NEXT_FILTER = "EXECUTE_NEXT_FILTER";
    private static final String PORTAL_LOGIN_URL = "/index.html";
    private LoginTicketMapper loginTicketMapper;
    private AccountMapper accountMapper;


    /**
     * 解决过滤器在SpringMVC之前运行
     *
     * @param filterConfig 配置属性
     */
    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        loginTicketMapper = (LoginTicketMapper) context.getBean("loginTicketMapper");
        accountMapper = (AccountMapper) context.getBean("accountMapper");
        logger.info("===============登录验证过滤器开始初始化============");
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 1.判断是否要继续执行下一个Filter,executeNextFilter值为false的话就直接放行到下一个Filter
        Boolean executeNextFilter = (Boolean) req.getAttribute("EXECUTE_NEXT_FILTER");
        if (!executeNextFilter) {
            // 1.1 如果发现了IS_LOGIN这个标记为true的话就说明是自动登录状态
            logger.info("【RequireLoginFilter】当前过滤器直接放行");
            chain.doFilter(req, resp);
        } else if (req.getAttribute("Account") != null ) {
            // 如果session中由用户就直接放行到下一个
            request.setAttribute(EXECUTE_NEXT_FILTER, true);
            chain.doFilter(request, response);
        } else {
            // 2.   判断是否有登录标记ticket,ticket是从Cookie中进行获取,循环遍历验证室友存在ticket值
            String ticket = LoginTicketUtils.getTicket(request);
            // 3.判断登录标记是否过期,不过期就自动登录,过期就需要重新登录
            if (ticket != null) {
                // 3.1  根据ticket字符串去数据库里面查询是否有这个,防止客户端伪造ticket
                LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
                // 3.2  如果查出来数据库里面没有这个ticket或者是已经过期了的话就让它重新登录
                if (loginTicket == null || LoginTicketUtils.isExpired(loginTicket)) {
                    response.sendRedirect(((HttpServletRequest) req).getContextPath()+PORTAL_LOGIN_URL);
                    logger.warn("【RequireLoginFilter】ticket过期了或者是前端伪造的了,强制需要重新登录,重定向到登录页面");
                    req.setAttribute(EXECUTE_NEXT_FILTER, false);
                    chain.doFilter(req, resp);
                } else {
                    // 3.3 到这里说明ticket是还没有过期的,根据数据库中login_ticket表中的账号去查询账号信息
                    logger.info("【RequireLoginFilter】有效的ticket值为【{}】,直接为登录状态,发送到下一个过滤器", ticket);
                    req.setAttribute(EXECUTE_NEXT_FILTER, true);
                    chain.doFilter(req, resp);
                }
            } else {
                // 4. 没有登录过,直接跳转到登录页面
                request.getRequestDispatcher(PORTAL_LOGIN_URL).forward(request, response);
                logger.warn("【RequireLoginFilter】请求头没有携带ticket,需要重新登录");
            }
        }

    }


    @Override
    public void destroy() {

    }
}
