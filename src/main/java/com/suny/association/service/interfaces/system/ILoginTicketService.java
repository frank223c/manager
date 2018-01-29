package com.suny.association.service.interfaces.system;

import com.suny.association.entity.po.LoginHistory;
import com.suny.association.entity.po.LoginTicket;
import com.suny.association.service.IBaseService;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 18-1-28.下午5:00
 *  @version 1.0
 **************************************/
public interface ILoginTicketService extends IBaseService<LoginTicket> {
    LoginTicket selectByTicket(String ticket);
}
