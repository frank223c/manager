package com.suny.association.service.interfaces;

import com.suny.association.entity.po.LoginTicket;
import com.suny.association.service.IBaseService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author  孙建荣
 * on 17-9-21.上午8:07
 */
public interface ILoginService extends IBaseService<LoginTicket> {
   Map<String, Object> login(String username, String password);
}
