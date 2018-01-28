package com.suny.association.service.impl.system;

import com.suny.association.entity.po.LoginTicket;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.mapper.LoginTicketMapper;
import com.suny.association.service.interfaces.system.ILoginticketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**************************************
 *  Description  
 *  @author 孙建荣
 *  @date 18-1-28.下午5:01
 *  @version 1.0
 **************************************/
@Service
public class LoginticketServiceImpl implements ILoginticketService {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Override
    public void insert(LoginTicket loginTicket) {
        loginTicketMapper.insert(loginTicket);
    }

    @Override
    public void deleteById(long id) {
        loginTicketMapper.deleteById(id);
    }

    @Override
    public void update(LoginTicket loginTicket) {
        loginTicketMapper.update(loginTicket);
    }

    @Override
    public LoginTicket selectById(long id) {
        return loginTicketMapper.selectById(id);
    }

    @Override
    public LoginTicket selectByName(String name) {
        return loginTicketMapper.selectByName(name);
    }

    @Override
    public int selectCount() {
        return loginTicketMapper.selectCount();
    }

    @Override
    public List<LoginTicket> selectAll() {
        return loginTicketMapper.selectAll();
    }

    @Override
    public List<LoginTicket> selectByParam(ConditionMap<LoginTicket> conditionMap) {
        return loginTicketMapper.selectByParam(conditionMap);
    }

    @Override
    public LoginTicket selectByTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }
}
