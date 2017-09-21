package com.suny.association.filter;

import com.suny.association.mapper.AccountMapper;
import com.suny.association.mapper.LoginTicketMapper;
import com.suny.association.pojo.po.Account;
import com.suny.association.pojo.po.HostHolder;
import com.suny.association.pojo.po.LoginTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by 孙建荣 on 17-9-20.上午9:17
 */
public class RequireLoginFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(RequireLoginFilter.class);
    private static final String LOGIN_URL = "/base/login.html";
    private HostHolder hostHolder;
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
        hostHolder = (HostHolder) context.getBean("hostHolder");
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
            logger.info("EXECUTE_NEXT_FILTER值为{},直接放行到下一个Filter", executeNextFilter);
            chain.doFilter(req, resp);
        } else {
            // 2.   判断是否有登录标记ticket,ticket是从Cookie中进行获取,循环遍历验证室友存在ticket值
            String ticket = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("ticket")) {
                        ticket = cookie.getValue();
                        break;
                    }
                }
            }
            // 3.判断登录标记是否过期,不过期就自动登录,过期就需要重新登录
            if (ticket != null) {
                // 3.1  根据ticket字符串去数据库里面查询是否有这个,防止客户端伪造ticket
                LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
                // 3.2  如果查出来数据库里面没有这个ticket或者是已经过期了的话就让它重新登录
                if (loginTicket == null || loginTicket.getExpired().isBefore(LocalDateTime.now()) || loginTicket.getStatus() != 1) {
                    logger.info("ticket过期时间为{},当前时间为{}", loginTicket != null ? loginTicket.getExpired().getNano() : 0, LocalTime.now().getNano());
                    logger.warn("cookie中的ticket{}已经过期了,需要重新登录,重定向到登录页面", ticket);
                    response.sendRedirect(LOGIN_URL);
                }
                // 3.3 到这里说明ticket是还没有过期的,根据数据库中login_ticket表中的账号去查询账号信息
                Account account = null;
                if (loginTicket != null) {
                    account = accountMapper.queryByLongId((long) loginTicket.getAccountId());
                }
                // 3.4 把账号信息放到对应的线程变量里面去,然后将请求发到下一个Filter中
                hostHolder.setAccounts(account);
                logger.info("有效的ticket值为{},直接为登录状态,发送到权限验证过滤器", ticket);
                chain.doFilter(req, resp);
            }
            // 4. 没有登录过,直接跳转到登录页面
            response.sendRedirect(LOGIN_URL);
        }

    }


    @Override
    public void destroy() {

    }
}
