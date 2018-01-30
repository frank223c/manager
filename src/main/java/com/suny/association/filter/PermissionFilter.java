package com.suny.association.filter;


import com.suny.association.cache.Cache;
import com.suny.association.cache.CacheManager;
import com.suny.association.mapper.AccountMapper;
import com.suny.association.mapper.LoginTicketMapper;
import com.suny.association.entity.po.*;
import com.suny.association.service.interfaces.system.IAccessPermissionService;
import com.suny.association.service.interfaces.system.IPermissionAllotService;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Comments:   权限控制过滤器
 *
 * @author :   孙建荣
 *         Create Date: 2017/05/11 21:28
 */
public class PermissionFilter implements Filter {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PermissionFilter.class);
    private static final String TICKET_SPLIT_SYMBOL = ":";
    private static final String USER_TICKET = "user_ticket";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String PORTAL_LOGIN_URL = "/login.html";
    private static final String NO_PERMISSION = "/403.jsp";
    public static final String PERMISSION_CACHE_PREFIX = "PERMISSION_";
    private IPermissionAllotService permissionAllotService;
    private IAccessPermissionService accessPermissionService;
    private LoginTicketMapper loginTicketMapper;
    private AccountMapper accountMapper;


    /**
     * 解决过滤器在SpringMVC之前运行
     *
     * @param filterConfig 配置属性
     */
    @Override
    public void init(FilterConfig filterConfig) {
        ServletContext servletContext = filterConfig.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        permissionAllotService = (IPermissionAllotService) context.getBean("permissionAllotService");
        accessPermissionService = (IAccessPermissionService) context.getBean("accessPermissionService");
        loginTicketMapper = (LoginTicketMapper) context.getBean("loginTicketMapper");
        accountMapper = (AccountMapper) context.getBean("accountMapper");
        logger.info("===============权限验证过滤器开始初始化============");
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 1.判断是否要继续执行下一个Filter,executeNextFilter值为false的话就直接放行到下一个Filter
        Boolean executeNextFilter = (Boolean) req.getAttribute("EXECUTE_NEXT_FILTER");
        if (!executeNextFilter) {
            logger.info("【PermissionFilter】当前过滤器直接放行");
            chain.doFilter(req, resp);
        } else {
            String reqURI = request.getRequestURI();
            String ticket = (String) request.getAttribute(USER_TICKET);
            int point = ticket.indexOf(TICKET_SPLIT_SYMBOL);
            String username = ticket.substring(0, point);
            // 2.1 判断是否有对应的权限
            boolean isPermission = isPermission(username, reqURI);
            //    2.1.1 有权限则放行到下一个Filter
            if (isPermission) {
                chain.doFilter(request, response);
            } else {
                // 2.1.2   重定向到无权限页面友情提示页面
                response.sendRedirect(NO_PERMISSION);
            }
        }

    }

    /**
     * 验证用户是否有这个权限操作当前的URL地址.
     *
     * @param username 用户账号名.
     * @param path     访问的URL地址.
     * @return 有访问这个URL地址的权限就返回true, 否则就返回false
     */
    private boolean isPermission(String username, String path) {
        Cache cache = CacheManager.getCache(PERMISSION_CACHE_PREFIX + username);
        List<PermissionAllot> allotList = (List<PermissionAllot>) cache.getValue();
        // 查看缓存中是否有这个用户的权限缓存,有就直接匹配,没有就去数据库查询
        if (allotList != null) {
            // 这里就对权限进行判断
        }
        // 等于空就是缓存里面没有值,先从数据库查询一次,然后再放到缓存里面去

        Account account = accountMapper.selectByName(username);
        //   1. 得到账号的角色,然后查询到用户对应的角色,    注意!这里使用的是单用户单角色
        List<AccountRoles> accountRolesList = account.getAccountRolesList();
        //   2. 取出所有用户拥有的权限,放到HashSet里面去重
        AtomicReference<HashSet<String>> permissions = new AtomicReference<>(new HashSet<>());
        for (AccountRoles accountRoles : accountRolesList) {
            List<Permission> permissionList = accountRoles.getPermissionList();
            for (Permission permission : permissionList) {
                String permissionName = permission.getpermissionName();
                permissions.get().add(permissionName);
            }
        }
        //   3. 首先进行判断，防止角色没有权限导致数据下标溢出
        if (!permissions.get().isEmpty()) {
            // 3.3 查询当前访问的URL需要什么权限
            AccessPermission accessPermission = accessPermissionService.selectByName(path);
            // 3.4 前面虽然已经判断了一次,但是有些URL也是可以匿名访问的
            if (accessPermission == null) {
                logger.info("这个页面不需要权限就可以访问");
                return true;
            } else {
                //  3.4.1 从权限实体中获取当前操作所需要得到权限字符串
                String urlPermission = accessPermission.getAccessPermission();
                //  3.4.2 定义一个标记,在循环中使用
                boolean hasPermission;
                //  3.4.3  循环匹配每一个权限实体里面的权限字符串跟请求的权限字符串是否相等,如果为true就直接跳出循环
                AtomicReference<Iterator<String>> iterator = new AtomicReference<>(permissions.get().iterator());
                if(iterator.get().hasNext()){
                    String permissionName = iterator.get().next();
                    hasPermission = permissionName.equals(urlPermission);
                    if (hasPermission) {
                        logger.info("访问当前页面需要【{}】用户{}有访问【{}】这个权限，放行", urlPermission, account.getAccountName(), urlPermission);
                        return true;
                    }
                }
                logger.warn("用户{}没有访问这个操作的权限", account.getAccountName());
                return false;
            }
        }
        logger.error("用户{}没有任何操作权限", account.getAccountName());
        return false;
    }


    @Override
    public void destroy() {
        logger.warn("权限验证过滤器开始销毁");
    }

}





























