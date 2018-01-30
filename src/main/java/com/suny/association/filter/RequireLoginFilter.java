package com.suny.association.filter;

import com.suny.association.entity.po.LoginTicket;
import com.suny.association.service.interfaces.system.ILoginTicketService;
import com.suny.association.utils.JedisAdapter;
import com.suny.association.utils.LoginTicketUtil;
import com.suny.association.utils.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 强制登录过滤器
 *
 * @author 孙建荣
 * @date 17-9-20
 */
public class RequireLoginFilter implements Filter {
    private static final String TICKET_SPLIT_SYMBOL = ":";
    private static final String USER_TICKET = "user_ticket";
    private static Logger logger = LoggerFactory.getLogger(RequireLoginFilter.class);
    private static final String EXECUTE_NEXT_FILTER = "EXECUTE_NEXT_FILTER";
    private static final String PORTAL_LOGIN_URL = "/login.html";
    private ILoginTicketService loginTicketService;
    private JedisAdapter jedisAdapter;


    /**
     * 解决过滤器在SpringMVC之前运行
     *
     * @param filterConfig 配置属性
     */
    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        loginTicketService = (ILoginTicketService) context.getBean("loginTicketServiceImpl");
        jedisAdapter = (JedisAdapter) context.getBean("jedisAdapter");
        logger.info("===============登录验证过滤器开始初始化============");
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 1.判断是否要继续执行下一个Filter,executeNextFilter值为false的话就直接放行到下一个Filter
        Boolean executeNextFilter = (Boolean) req.getAttribute("EXECUTE_NEXT_FILTER");
        if (!executeNextFilter) {
            // 1.1 如果发现了EXECUTE_NEXT_FILTERN这个标记为true的话就说明是需要直接放行状态
            logger.info("【RequireLoginFilter】当前过滤器直接放行");
            chain.doFilter(req, resp);
        } else {
            // 2.   判断是否有登录标记ticket,ticket是从Cookie中进行获取,循环遍历验证室友存在ticket值
            String ticket = LoginTicketUtil.getTicket(request);
            // 3.判断登录标记是否过期,不过期就自动登录,过期就需要重新登录
            if (ticket != null && hasValidTicket(request)) {
                // ticket过期了就送的去登录
                // 3.3 到这里说明ticket是还没有过期的,根据数据库中login_ticket表中的账号去查询账号信息
                logger.info("【RequireLoginFilter】有效的ticket值为【{}】,直接为登录状态,发送到下一个过滤器", ticket);
                req.setAttribute(USER_TICKET,ticket);
                req.setAttribute(EXECUTE_NEXT_FILTER, true);
                chain.doFilter(req, resp);
            } else {
                // 4. 要么就没有登录过,要么就ticket过期了,直接跳转到登录页面
                request.getRequestDispatcher(PORTAL_LOGIN_URL).forward(request, response);
                req.setAttribute(EXECUTE_NEXT_FILTER, false);
                chain.doFilter(req, resp);
                logger.warn("【RequireLoginFilter】ticket无效或者没有,需要重新登录");
            }
        }

    }

    /**
     * 判断ticket是否有效
     *
     * @param request 请求
     * @return 有效则为true, 无效则为false
     */
    @SuppressWarnings("Duplicates")
    private boolean hasValidTicket(HttpServletRequest request) {
        String ticket = LoginTicketUtil.getTicket(request);
        // 当cookie中ticket不为空的时候才去查询是否ticket有效
        if (ticket != null) {
            int point = ticket.indexOf(TICKET_SPLIT_SYMBOL);
            String username = ticket.substring(0, point);
            String redisTicket = jedisAdapter.get(RedisKeyUtils.getLoginticket(username));
            // 如果redis里面存在对应用户的ticket
            if (redisTicket != null && !Objects.equals(redisTicket, "")) {
                long expireTime = jedisAdapter.getExpireTime(RedisKeyUtils.getLoginticket(username));
                if (expireTime > 0) {
                    // redis里面读取用户信息成功,直接放行登录
                    return true;
                }
            }
            // Redis中不存在才去关系数据库中查询
            LoginTicket loginTicket = loginTicketService.selectByTicket(ticket);
            // 这里防止前端伪造ticket的情况,数据库中不存在这个
            return loginTicket != null && loginTicket.getExpired().isAfter(LocalDateTime.now());
        }
        // cookie里面没有ticket就直接返回false
        return false;
    }


    @Override
    public void destroy() {

    }
}
