package com.suny.association.service.impl;

import com.suny.association.enums.OperateTypeEnum;
import com.suny.association.mapper.AccountMapper;
import com.suny.association.mapper.LoginTicketMapper;
import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.LoginTicket;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.interfaces.ILoginService;
import com.suny.association.service.interfaces.system.ILoginHistoryService;
import com.suny.association.utils.JedisAdapter;
import com.suny.association.utils.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
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
    private final JedisAdapter jedisAdapter;

    @Autowired
    public LoginServiceImpl(LoginTicketMapper loginTicketMapper, AccountMapper accountMapper, ILoginHistoryService loginHistoryService, JedisAdapter jedisAdapter) {
        this.loginTicketMapper = loginTicketMapper;
        this.accountMapper = accountMapper;
        this.loginHistoryService = loginHistoryService;
        this.jedisAdapter = jedisAdapter;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        //  1. 首先对参数进行各种非空判断,如果违规就直接返回给Controller
        Map<String, Object> map = new HashMap<>(16);
        if (!(validParam(username) || validParam(password))) {
            //   1.1 登录失败也保存登录日志,这样可与从数据库中获取短时间内登录次数非常多的账号
            loginHistoryService.saveLoginLog(username, false);
            map.put("msg", "参数不能为空");
            return map;
        }
        //  2.     ==========账号密码匹配成功的业务逻辑=============
        if (authAction(username, password)) {
            Account account = accountMapper.selectByName(username);
            //  2.1   查看数据库里面ticket是否存在
            String redisValidTicket = getRedisValidTicket(username);
            if (redisValidTicket != null) {
                map.put(TICKET, redisValidTicket);
            } else {
                // 到了这里说明Redis里面不存在对应的key了
                String mysqlValidTicket = getMysqlValidTicket(account);
                map.put(TICKET, mysqlValidTicket);
            }
            //  2.4 只要账号密码验证成功的话我们就可以说就已经是登录成功了的
            loginHistoryService.saveLoginLog(username, true);
            return map;
        }
        //  3     ===========账号密码匹配失败的业务逻辑开始==============
        //  3.1    保存登录失败的日志信息
        loginHistoryService.saveLoginLog(username, false);
        //  3.1.2    向controller返回信息
        map.put("msg", "账号密码匹配失败");
        return map;
    }

    /**
     * 从MYSQL数据库中查询数据库中的Ticket
     *
     * @param account 登录的账号信息
     * @return Ticket字符串
     */
    private String getMysqlValidTicket(Account account) {
        LoginTicket loginTicket = loginTicketMapper.selectByAccountId(account.getAccountId());
        //  2.2   数据库中存在这个用户的ticket
        if (loginTicket != null) {
            //  2.2.1  数据库中存在不过期的ticket,添加到Map里面返回给Controller
            if (loginTicket.getExpired().isAfter(LocalDateTime.now())) {
                // 把MYSQL里面的ticket添加到Redis里面
                mysqlTicketToRedisTicket(account,loginTicket.getExpired().getSecond(),loginTicket.getTicket());
                return loginTicket.getTicket();
            } else {
                //  2.2.2  数据库中存在已经过期的ticket,为了不删除ticket就直接更新过期时间跟状态,就直接更新
                operateTicket(account, OperateTypeEnum.UPDATE);
                //  2.2.3   把ticket返回给Controller
                return loginTicketMapper.selectByAccountId(account.getAccountId()).getTicket();
            }
            //  2.3  数据库中不存在对应用户的ticket
        } else {
            //          2.3.1 对登录的用户添加一个ticket
            operateTicket(account, OperateTypeEnum.INSERT);
            return loginTicketMapper.selectByAccountId(account.getAccountId()).getTicket();
        }
    }

    /**
     * 从Redis里面查看是否有当前用户有效的ticket
     *
     * @param username 用户名
     * @return Ticket字符串
     */
    private String getRedisValidTicket(String username) {
        // 先从redis里面取
        String redisTicket = jedisAdapter.get(RedisKeyUtils.getLoginTicketKey(username));
        // 如果redis里面存在对应用户的ticket
        if (redisTicket != null && !Objects.equals(redisTicket, "")) {
            long expireTime = jedisAdapter.getExpireTime(RedisKeyUtils.getLoginTicketKey(username));
            if (expireTime > 0) {
                // redis里面读取用户信息成功,直接放行登录
                return redisTicket;
            }
        }
        return null;
    }


    private boolean validParam(String param) {
        return !("".equals(param) || param.length() == 0);
    }

    /**
     * 操作不同数据库中的ticket,更新或者是新增
     *
     * @param account         登录账号
     * @param operateTypeEnum 操作类型
     */
    private void operateTicket(Account account, OperateTypeEnum operateTypeEnum) {
        int expireSeconds = 604800;
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setAccountId(account.getAccountId());
        LocalDateTime expired = LocalDateTime.now().plusSeconds(expireSeconds);
        loginTicket.setExpired(expired);
        // 状态为0则表示不过期，过期则为0
        loginTicket.setStatus(0);
        loginTicket.setTicket(account.getAccountName()+":"+UUID.randomUUID().toString().replaceAll("-", ""));
        if (operateTypeEnum == OperateTypeEnum.INSERT) {
            loginTicketMapper.insert(loginTicket);
        } else if (operateTypeEnum == OperateTypeEnum.UPDATE) {
            loginTicketMapper.update(loginTicket);
        }
        // 对Redis来说,Mysql数据库里面的Ticket无论是新增还是更新,在Redis里面都是已经被删除了的
        mysqlTicketToRedisTicket(account, expireSeconds, loginTicket.getTicket());
    }

    /**
     *  把MSQL中的ticket同步到Redis里面
     * @param account    登录用户信息
     * @param expireSeconds   过期时间
     * @param stringTicket  登录ticket字符串
     */
    private void mysqlTicketToRedisTicket(Account account, int expireSeconds, String stringTicket) {
        String redisTicketKeyName = RedisKeyUtils.getLoginTicketKey(account.getAccountName());
        jedisAdapter.set(redisTicketKeyName, stringTicket);
        jedisAdapter.setExpire(redisTicketKeyName, expireSeconds);
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






















