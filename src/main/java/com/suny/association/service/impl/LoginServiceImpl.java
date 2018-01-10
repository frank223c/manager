package com.suny.association.service.impl;

import com.suny.association.enums.OperateTypeEnum;
import com.suny.association.mapper.AccountMapper;
import com.suny.association.mapper.LoginTicketMapper;
import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.LoginTicket;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.interfaces.ILoginService;
import com.suny.association.service.interfaces.system.ILoginHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author 孙建荣
 * @date 17-9-21
 */
@Service
public class LoginServiceImpl implements ILoginService {
    private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    private static final String TICKET = "ticket";

    private final LoginTicketMapper loginTicketMapper;
    private final AccountMapper accountMapper;
    private final ILoginHistoryService loginHistoryService;

    @Autowired
    public LoginServiceImpl(LoginTicketMapper loginTicketMapper, AccountMapper accountMapper, ILoginHistoryService loginHistoryService) {
        this.loginTicketMapper = loginTicketMapper;
        this.accountMapper = accountMapper;
        this.loginHistoryService = loginHistoryService;
    }

    @Override
    public AtomicReference<Map<String, Object>> login(String username, String password) {
        //  1. 首先对参数进行各种非空判断,如果违规就直接返回给Controller
        AtomicReference<Map<String, Object>> map = new AtomicReference<>(new HashMap<>(16));
        if (!(validParam(username) || validParam(password))) {
            //   1.1 登录失败也保存登录日志,这样可与从数据库中获取短时间内登录次数非常多的账号
            loginHistoryService.saveLoginLog(username, false);
            map.get().put("msg", "参数不能为空");
            return map;
        }
        //  2.     ==========账号密码匹配成功的业务逻辑=============
        if (authAction(username, password)) {
            Account account = accountMapper.selectByName(username);
            //  2.1   查看数据库里面ticket是否存在
            LoginTicket loginTicket = loginTicketMapper.selectByAccountId(account.getAccountId());
            //  2.2   数据库中存在这个用户的ticket
            if (loginTicket != null) {
                //  2.2.1  数据库中存在不过期的ticket,添加到Map里面返回给Controller
                if (loginTicket.getExpired().isAfter(LocalDateTime.now())) {
                    // 把ticket添加到Redis里面
                    map.get().put(TICKET, loginTicket.getTicket());
                } else {
                    //  2.2.2  数据库中存在已经过期的ticket,为了不删除ticket就直接更新过期时间跟状态,就直接更新
                    operateTicket(account.getAccountId(), OperateTypeEnum.UPDATE);
                    //  2.2.3   把ticket返回给Controller
                    map.get().put(TICKET, loginTicketMapper.selectByAccountId(account.getAccountId()));
                }
                //  2.3  数据库中不存在对应用户的ticket
            } else {
                //          2.3.1 对登录的用户添加一个ticket
                operateTicket(account.getAccountId(), OperateTypeEnum.INSERT);
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
        return !("".equals(param) || param.length() == 0);
    }

    private void operateTicket(long accountId, OperateTypeEnum operateTypeEnum) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setAccountId(accountId);
        LocalDateTime expired = LocalDateTime.now().plusHours(148);
        loginTicket.setExpired(expired);
        // 状态为0则表示不过期，过期则为0
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        if (operateTypeEnum == OperateTypeEnum.INSERT) {
            logger.warn("插入一条ticket值{}",loginTicket.toString());
            loginTicketMapper.insert(loginTicket);
        } else if (operateTypeEnum == OperateTypeEnum.UPDATE) {
             logger.warn("更新数据库中的ticket值{}",loginTicket.toString());
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
        Account account = accountMapper.selectByName(username);
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
    public LoginTicket selectById(long id) {
        return null;
    }

    @Override
    public LoginTicket selectByName(String name) {
        return null;
    }

    @Override
    public int selectCount() {
        return 0;
    }

    @Override
    public List<LoginTicket> selectAll() {
        return null;
    }

    @Override
    public List<LoginTicket> selectByParam(ConditionMap<LoginTicket> conditionMap) {
        return loginTicketMapper.selectByParam(conditionMap);
    }


}






















