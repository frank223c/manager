package com.suny.association.utils;

import com.suny.association.mapper.AccountMapper;
import com.suny.association.mapper.LoginTicketMapper;
import com.suny.association.pojo.po.Account;
import com.suny.association.pojo.po.LoginTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import sun.security.krb5.internal.Ticket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 本项目中专门使用的ticket工具
 * Created by 孙建荣 on 17-9-22.上午11:19
 */
public class LoginTicketUtils {
    private static Logger logger = LoggerFactory.getLogger(LoginTicketUtils.class);
    private static LoginTicketMapper loginTicketMapper = (LoginTicketMapper) ApplicationContextHolder.get().getBean("loginTicketMapper");
    private static AccountMapper accountMapper = (AccountMapper) ApplicationContextHolder.get().getBean("accountMapper");

    private LoginTicketUtils() {
    }

    /**
     * 直接通过一个ticket查询对应的Account信息
     *
     * @param ticket ticket标示
     * @return 如果ticket在数据库中存在就会去查询Account, 不存在就直接返回null
     */
    public static Account selectAccount(String ticket) {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
        if (loginTicket != null) {
            return accountMapper.selectById(loginTicket.getId());
        }
        logger.warn("ticket{}在数据库中不存在,可能是客户端伪造", ticket);
        return null;
    }

    /**
     * 检查ticket是否存在
     *
     * @param ticket ticket标示
     * @return 查询数据库中是否存在这个ticket, 存在返回true, 没有返回false
     */
    public static Boolean checkExits(String ticket) {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
        return loginTicket != null;
    }


    /**
     * 直接从request请求里面取出ticket
     *
     * @param request 请求头
     * @return 如果request中存在ticket标记的话就会返回, 否则就会返回空
     */
    public static String getTicket(HttpServletRequest request) {
        String ticket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
            return ticket;
        }
        return null;
    }


    public static boolean isExpired(LoginTicket loginTicket) {
        return loginTicket.getExpired().isBefore(LocalDateTime.now()) || loginTicket.getStatus() == 1;
    }
}






































