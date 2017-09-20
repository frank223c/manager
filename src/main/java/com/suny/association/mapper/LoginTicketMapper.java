package com.suny.association.mapper;

import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.pojo.po.LoginTicket;

/**
 * Created by 孙建荣 on 17-9-20.上午8:55
 */
public interface LoginTicketMapper extends IMapper<LoginTicket> {
    LoginTicket selectByAccountId(int accountId);
    LoginTicket selectByTicket(String ticket);
}
