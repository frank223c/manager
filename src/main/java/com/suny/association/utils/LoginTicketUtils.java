package com.suny.association.utils;

import com.suny.association.pojo.po.LoginTicket;
import sun.security.krb5.internal.Ticket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created by 孙建荣 on 17-9-22.上午11:19
 */
public class LoginTicketUtils {
    private LoginTicketUtils() {
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






































