package com.suny.association.utils;

import com.suny.association.entity.po.LoginTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 本项目中专门使用的ticket工具
 *
 * @author 孙建荣
 * @date 17-9-22
 */
public class LoginTicketUtil {
    private static Logger logger = LoggerFactory.getLogger(LoginTicketUtil.class);

    private LoginTicketUtil() {
    }

    /**
     * 直接从request请求里面取出ticket
     *
     * @param request 请求头
     * @return 如果request中存在ticket标记的话就会返回, 否则就会返回空
     */
    public static String getTicketFormCookie(HttpServletRequest request) {
        String ticket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("ticket".equals(cookie.getName())) {
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






































