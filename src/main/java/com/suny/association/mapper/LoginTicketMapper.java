package com.suny.association.mapper;

import com.suny.association.mapper.interfaces.IMapper;
import com.suny.association.pojo.po.LoginTicket;

/**
 *
 * @author 孙建荣
 * @date 17-9-20
 */
public interface LoginTicketMapper extends IMapper<LoginTicket> {

    LoginTicket selectByAccountId(long accountId);
    LoginTicket selectByTicket(String ticket);
}
