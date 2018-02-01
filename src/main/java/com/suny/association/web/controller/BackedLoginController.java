package com.suny.association.web.controller;

import com.suny.association.annotation.SystemControllerLog;
import com.suny.association.common.RequestHolder;
import com.suny.association.entity.dto.ResultDTO;
import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.LoginTicket;
import com.suny.association.entity.po.Member;
import com.suny.association.enums.LoginEnum;
import com.suny.association.service.interfaces.IAccountService;
import com.suny.association.service.interfaces.ILoginService;
import com.suny.association.service.interfaces.system.ILoginTicketService;
import com.suny.association.utils.JedisAdapter;
import com.suny.association.utils.LoginTicketUtil;
import com.suny.association.utils.RedisKeyUtils;
import com.suny.association.utils.TokenProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Comments:   基础公共Controller
 *
 * @author :   孙建荣
 *         Create Date: 2017/03/05 11:05
 */
@Controller
public class BackedLoginController {
    private static final String TICKET_SPLIT_SYMBOL = ":";
    private static Logger logger = LoggerFactory.getLogger(BackedLoginController.class);
    private static final String TICKET = "ticket";
    private static final String ACCOUNT_ATTRIBUTE = "account";
    private static final String MEMBER_ATTRIBUTE = "member";
    private final IAccountService accountService;
    private final ILoginService loginService;
    private final ILoginTicketService loginTicketService;
    private static final String TOKEN = "token";
    @Autowired
    private JedisAdapter jedisAdapter;

    @Autowired
    public BackedLoginController(IAccountService accountService, ILoginService loginService, ILoginTicketService loginTicketService) {
        this.accountService = accountService;
        this.loginService = loginService;
        this.loginTicketService = loginTicketService;
    }


    /**
     * 登录页面
     */
    @RequestMapping(value = {"/", "/admin.html", "/login.html"}, method = RequestMethod.GET)
    public String backendLogin(HttpServletRequest request) {
        // 如果有没有过期的ticket直接去管理员页面
        if (hasValidTicket(request)) {
            return "backend/admin";
        }
        String token = TokenProcessor.getInstance().makeToken();
        request.getSession().setAttribute(TOKEN, token);
        logger.info("产生的令牌值是 {}", token);
        return "login";
    }

    /**
     * 全局错误页面
     */
    @RequestMapping(value = "/error.html", method = RequestMethod.GET)
    public ModelAndView errorPage() {
        return new ModelAndView("error");
    }


    /**
     * 提交登录信息操作
     *
     * @param username 登录用户名
     * @param password 登录密码
     * @param formCode 表单过来的验证码
     * @param request  request请求
     * @return 验证的json结果
     */
    @RequestMapping(value = "/login.action", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO loginAction(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("formCode") String formCode,
                                 @RequestParam(value = "rememberMe", defaultValue = "true") Boolean rememberMe,
                                 @RequestParam("token") String token,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        //   1.2    获取session中保存的本次服务器下发的验证码
        String sessionCode = (String) request.getSession().getAttribute("imageCode");
        //   1.3    匹配session里面的验证码跟表单上的验证码是否相等，这里为了开发方便就先关闭
        if ("".equals(formCode) || !sessionCode.equals(formCode)) {
            return ResultDTO.failureResult(LoginEnum.VALIDATE_CODE_ERROR);
        }

        //    1.  首先验证表单提交的token跟session里面的token是否相等，相等就说明不是重复提交
        if (!isRepeatSubmit(token, request)) {
            //   1.1   把session里面的token标记先移除
            request.getSession().removeAttribute(TOKEN);
            //   1.4   获取登录的结果,也就是带有ticket则表示登录成功了
            Map<String, Object> loginResult = loginService.login(username, password);
            if (loginResult.containsKey(TICKET)) {
                if (rememberMe) {
                    //   1.4.1    把获取到的ticket放到Cookie中去
                    String ticket = (String) loginResult.get(TICKET);
                    Cookie cookie = new Cookie(TICKET, ticket);
                    cookie.setPath("/");
                    cookie.setMaxAge(3600 * 24 * 7);
                    // cookie.setSecure(true); 在http中是客户端cookie无效的；在https中才有效。请注意这里在HTTP连接中会导致导致客户端的cookie无法传输
                    response.addCookie(cookie);
                }
                //    1.4.4   把一些进入主页面需要的数据先放进去
                saveUser(request, response, username);
                logger.warn("登录成功了,给前端发送通知");
                return ResultDTO.successResult(LoginEnum.LOGIN_SYSTEM);
            }
            //   1.5   没有返回ticket就是登录失败了,可能是由于面膜错误,账号错误，账号密码不匹配，参数为空等等
            return ResultDTO.failureResult(LoginEnum.UNKNOWN_ERROR);
        }
        //   2.  重复提交表单的业务逻辑处理
        logger.warn("重复提交表单");
        return ResultDTO.failureResult(LoginEnum.REPEAT_SUBMIT);
    }


    /**
     * 验证token是否相同，防止CSRF或者重复提交表单
     *
     * @param token   令牌值
     * @param request request值
     * @return 比较的结果
     */
    private boolean isRepeatSubmit(String token, HttpServletRequest request) {
        // 如果token是空的则说明重复提交了表单
        if ("".equals(token) || token == null) {
            return true;
        }
        String sessionToken = (String) request.getSession().getAttribute(TOKEN);
        return sessionToken == null || (sessionToken.equals(token) ? false : false);
    }


    /**
     * 验证成功后保存用户的登录信息
     *
     * @param response response请求
     * @param username 登录的用户名
     */
    private void saveUser(HttpServletRequest request, HttpServletResponse response, String username) {
        Account accountHolder = RequestHolder.getAccountHolder();
        if (accountHolder != null) {
            request.getSession().setAttribute(ACCOUNT_ATTRIBUTE, accountHolder);
        } else {
            Account account = accountService.selectByName(username);
            RequestHolder.add(account);
            request.getSession().setAttribute(ACCOUNT_ATTRIBUTE, account);
        }

    }


    /**
     * 判断ticket是否有效
     *
     * @param request 请求
     * @return 有效则为true, 无效则为false
     */
    @SuppressWarnings("Duplicates")
    private boolean hasValidTicket(HttpServletRequest request) {
        String ticket = LoginTicketUtil.getTicket(request);
        // 当cookie中ticket不为空的时候才去查询是否ticket有效
        if (ticket != null) {
            int point = ticket.indexOf(TICKET_SPLIT_SYMBOL);
            String username = ticket.substring(0, point);
            String redisTicket = jedisAdapter.get(RedisKeyUtils.getLoginticket(username));
            // 如果redis里面存在对应用户的ticket
            if (redisTicket != null && !Objects.equals(redisTicket, "")) {
                long expireTime = jedisAdapter.getExpireTime(RedisKeyUtils.getLoginticket(username));
                if (expireTime > 0) {
                    // redis里面读取用户信息成功,直接放行登录
                    return true;
                }
            }
            // Redis中不存在才去关系数据库中查询
            LoginTicket loginTicket = loginTicketService.selectByTicket(ticket);
            // 这里防止前端伪造ticket的情况,数据库中不存在这个
            return loginTicket != null && loginTicket.getExpired().isAfter(LocalDateTime.now());
        }
        // cookie里面没有ticket就直接返回false
        return false;
    }


    /**
     * 用户点击退出的操作
     *
     * @return 注销登录，然后返回注销结果
     */
    @SystemControllerLog(description = "注销操作")
    @RequestMapping(value = "/logout.action", method = RequestMethod.GET)
    @ResponseBody
    public ResultDTO logout(HttpServletRequest request) {
        // 防止自动创建session，传入false阻止自动创建
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(ACCOUNT_ATTRIBUTE);
            session.removeAttribute(MEMBER_ATTRIBUTE);
            return ResultDTO.successResult(LoginEnum.LOGOUT_SUCCESS);
        }
        request.getSession().removeAttribute(TICKET);
        return ResultDTO.failureResult(LoginEnum.LOGOUT_FAIL);
    }


}









