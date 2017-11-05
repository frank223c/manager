package com.suny.association.service.impl.system;

import com.suny.association.annotation.SystemServiceLog;
import com.suny.association.mapper.LoginHistoryMapper;
import com.suny.association.entity.po.LoginHistory;
import com.suny.association.entity.vo.ConditionMap;
import com.suny.association.service.AbstractBaseServiceImpl;
import com.suny.association.service.interfaces.system.ILoginHistoryService;
import com.suny.association.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.suny.association.utils.WebUtils.getClientIpAdder;
import static com.suny.association.utils.WebUtils.getClientOS;

/**
 * Comments:  登录历史记录业务逻辑
 * @author :   孙建荣
 * Create Date: 2017/03/07 22:34
 */
@Service
public class LoginHistoryServiceImpl extends AbstractBaseServiceImpl<LoginHistory> implements ILoginHistoryService {

    private LoginHistoryMapper loginHistoryMapper;

    @Autowired
    public LoginHistoryServiceImpl(LoginHistoryMapper loginHistoryMapper) {
        this.loginHistoryMapper = loginHistoryMapper;
    }

    public LoginHistoryServiceImpl() {
    }

    /**
     * 通过查询条件查询账号登录记录
     *
     * @param conditionMap 自己封装的查询条件
     * @return 带查询条件的登录记录
     */
    @Override
    public List<LoginHistory> selectByParam(ConditionMap<LoginHistory> conditionMap) {
        return loginHistoryMapper.selectByParam(conditionMap);
    }

    /*  通过登录用户名查询登录记录 */
    @Override
    public LoginHistory selectByName(String name) {
        return loginHistoryMapper.selectByName(name);
    }

    /* 查询数据库里面的登录记录条数  */
    @Override
    public int selectCount() {
        return loginHistoryMapper.selectCount();
    }

    /* 插入一条登录历史记录  */
    @SystemServiceLog(description = "插入登录历史记录失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(LoginHistory loginHistory) {
        loginHistoryMapper.insert(loginHistory);
    }

    /**
     * 根据条件查询数据库里面的登录记录条数
     *
     * @param loginHistory 实体类查询条件
     * @return 查询条件查询出来的记录
     */
    @Override
    public int selectCountByParam(LoginHistory loginHistory) {
        return loginHistoryMapper.selectCountByParam(loginHistory);
    }

    /**
     * 收集用户登录信息
     *
     * @param username  登录的用户名
     */
    @SystemServiceLog(description = "组装的登录信息插入失败")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLoginLog(String username, boolean authStatus) {
        // 1. 首先构造一个登录记录的实体
        LoginHistory loginHistory = new LoginHistory();
        //  2. 填充userAgent
        String userAgent = WebUtils.getHttpServletRequest().getHeader("user-agent");
        loginHistory.setLoginUserAgent(userAgent);
        //  3.  通过userAgent分析触登录的浏览器
        String loginBrowser = WebUtils.getBrowserInfo(userAgent);
        //  4. 填充用户登录的ip
        String loginIp = getClientIpAdder(WebUtils.getHttpServletRequest());
        loginHistory.setLoginIp(loginIp);
        //  5.  填充用户登录的时间
        loginHistory.setLoginTime(LocalDateTime.now());
        //  6. 填充登录的浏览器信息
        loginHistory.setLoginBrowser(loginBrowser);
        //  7.  填充登录用户的浏览器版本
        loginHistory.setLoginOsVersion(getClientOS(userAgent));
        //  8. 填充用户登录验证账号密码的状态   true为认证成功   false则为认证失败
        loginHistory.setLoginStatus(authStatus);
        //  9.  通过登录的用户名查询触对应的一条账号信息

        //  10. 填充 字段 登录用户
        loginHistory.setLoginName(username);
        //  11.通过ip地址去获取普通的定位地址
        WebUtils.IpInfo ipInfo = WebUtils.getIpInfo(loginIp);
        //   11.1 判断是否得到了通过定位到的IP得到的地址
        if(ipInfo != null){
            loginHistory.setLoginAddress(ipInfo.getCounty() + ipInfo.getArea() + ipInfo.getRegion() + ipInfo.getCity() + ipInfo.getIsp());
        }else{
            loginHistory.setLoginAddress("未知地址");
        }
        insert(loginHistory);
    }




    /**
     *  通过成员的id去查询一条登录历史记录
     * @param memberId   协会成员的ID
     * @return   登录信息
     */
    @Override
    public List<LoginHistory> queryLoginLogByMemberId(int memberId) {
        return loginHistoryMapper.queryLoginLogByMemberId(memberId);
    }


}
