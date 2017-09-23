package com.suny.association.service.impl;

import com.suny.association.enums.OperateType;
import com.suny.association.mapper.AccountMapper;
import com.suny.association.mapper.LoginTicketMapper;
import com.suny.association.pojo.po.Account;
import com.suny.association.pojo.po.HostHolder;
import com.suny.association.pojo.po.LoginTicket;
import com.suny.association.service.interfaces.ILoginService;
import com.suny.association.service.interfaces.system.ILoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by 孙建荣 on 17-9-21.上午8:09
 */
@Service
public class LoginServiceImpl implements ILoginService {
    private static final String TICKET = "ticket";

    private final LoginTicketMapper loginTicketMapper;
    private final AccountMapper accountMapper;
    private final HostHolder hostHolder;
    private final ILoginHistoryService loginHistoryService;

    @Autowired
    public LoginServiceImpl(LoginTicketMapper loginTicketMapper, AccountMapper accountMapper, HostHolder hostHolder, ILoginHistoryService loginHistoryService) {
        this.loginTicketMapper = loginTicketMapper;
        this.accountMapper = accountMapper;
        this.hostHolder = hostHolder;
        this.loginHistoryService = loginHistoryService;
    }

    @Override
    public AtomicReference<Map<String, Object>> login(String username, String password) {
        //  1. 首先对参数进行各种非空判断,如果违规就直接返回给Controller
        AtomicReference<Map<String, Object>> map = new AtomicReference<>(new HashMap<>());
        if (!(validParam(username) || validParam(password))) {
            //   1.1 登录失败也保存登录日志,这样可与从数据库中获取短时间内登录次数非常多的账号
            loginHistoryService.saveLoginLog(username, false);
            map.get().put("msg", "参数不能为空");
            return map;
        }
        //  2.     ==========账号密码匹配成功的业务逻辑=============
        if (authAction(username, password)) {
            Account account = accountMapper.queryByName(username);
            hostHolder.setAccounts(account);
            //  2.1   查看数据库里面ticket是否存在
            LoginTicket loginTicket = loginTicketMapper.selectByAccountId(account.getAccountId());
            //  2.2   数据库中存在这个用户的ticket
            if (loginTicket != null) {
                //  2.2.1  数据库中存在不过期的ticket,添加到Map里面返回给Controller
                if (loginTicket.getStatus() == 0 || loginTicket.getExpired().isAfter(LocalDateTime.now())) {
                    map.get().put(TICKET, loginTicket.getTicket());
                } else {
                    //  2.2.2  数据库中存在已经过期的ticket,为了不删除ticket就直接更新过期时间跟状态,就直接更新
                    operateTicket(account.getAccountId(), OperateType.UPDATE);
                    //  2.2.3   把ticket返回给Controller
                    map.get().put(TICKET, loginTicketMapper.selectByAccountId(account.getAccountId()));
                }
                //  2.3  数据库中不存在对应用户的ticket
            } else {
                //          2.3.1 对登录的用户添加一个ticket
                operateTicket(account.getAccountId(), OperateType.INSERT);
                map.get().put(TICKET, loginTicketMapper.selectByAccountId(account.getAccountId()));
            }
            //  2.4 只要账号密码验证成功的话我们就可以说就已经是登录成功了的
            loginHistoryService.saveLoginLog(username, true);
            return map;
        }
        //  3     ===========账号密码匹配失败的业务逻辑开始==============
        //  3.1    保存登录失败的日志信息
        loginHistoryService.saveLoginLog(username, false);
        //  3.1.2    向controller返回信息
        map.get().put("msg", "账号密码匹配失败");
        return map;
    }


    private boolean validParam(String param) {
        return !(param.equals("") || param.length() == 0);
    }

    private void operateTicket(long accountId, OperateType operateType) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setAccountId(accountId);
        LocalDateTime expired = LocalDateTime.now().plusHours(148);
        loginTicket.setExpired(expired);
        // 状态为0则表示不过期，过期则为0
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        if (operateType == OperateType.INSERT) {
            loginTicketMapper.insert(loginTicket);
        } else if (operateType == OperateType.UPDATE) {
            loginTicketMapper.update(loginTicket);
        }
    }


    /**
     * 认证用户的密码跟用户名
     *
     * @param username 用户名
     * @param password 密码
     */
    private boolean authAction(String username, String password) {
        Account account = accountMapper.queryByName(username);
//        return account != null && account.getAccountPassword().equals(EncryptUtil.encryptToMD5(password));
        return account != null && account.getAccountPassword().equals(password);

    }


    @Override
    public void insert(LoginTicket loginTicket) {

    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public void update(LoginTicket loginTicket) {

    }

    @Override
    public LoginTicket queryById(long id) {
        return null;
    }

    @Override
    public LoginTicket queryByName(String name) {
        return null;
    }

    @Override
    public int queryCount() {
        return 0;
    }

    @Override
    public List<LoginTicket> queryAll() {
        return null;
    }

    @Override
    public List<LoginTicket> list(Map<Object, Object> criteriaMap) {
        return null;
    }


}






















