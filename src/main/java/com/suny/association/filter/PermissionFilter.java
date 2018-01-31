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
import java.util.*;

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
    public static final String ACCESS_PERMISSION_CACHE_PREFIX = "ACCESS_PERMISSION_";
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
            boolean hasPermission;
            // 这里使用getRequestURI会携带项目名字,getServletPath不会携带项目名字
            String requestUrl = request.getServletPath();
            String ticket = (String) request.getAttribute(USER_TICKET);
            int point = ticket.indexOf(TICKET_SPLIT_SYMBOL);
            String username = ticket.substring(0, point);
            // 如果缓存中有权限信息就直接去缓存中取
            List<String> permissionFormCache = getPermissionFormCache(username);
            List<AccessPermission> accessPermissionListFormCache = getAccessPermissionListFormCache();
            // 当从缓存中取当前用户的权限以及系统所有操作权限都不为空时
            if (!permissionFormCache.isEmpty() && !accessPermissionListFormCache.isEmpty()) {
                hasPermission = hasPermission(permissionFormCache, accessPermissionListFormCache, requestUrl);
            } else {
                if (permissionFormCache.isEmpty() && accessPermissionListFormCache.isEmpty()) {
                    // 当从缓存中取当前用户的权限为空的时候,直接从MYSQL中查询用户的权限
                    List<String> permissionFormMYSQL = getPermissionFormMYSQL(username);
                    setPermissionToCache(permissionFormMYSQL, username);
                    // 当从缓存中取到系统中所有的的操作权限为空的时候,直接从MYSQL中查询用户的权限
                    List<AccessPermission> accessPermissionListFormMYSQL = getAccessPermissionListFormMYSQL();
                    setAccessPermissionListToCache(accessPermissionListFormMYSQL);
                    hasPermission = hasPermission(permissionFormMYSQL, accessPermissionListFormMYSQL, requestUrl);
                } else if (permissionFormCache.isEmpty()) {
                    // 当从缓存中取当前用户的权限为空的时候,直接从MYSQL中查询用户的权限
                    List<String> permissionFormMYSQL = getPermissionFormMYSQL(username);
                    setPermissionToCache(permissionFormMYSQL, username);
                    hasPermission = hasPermission(permissionFormMYSQL, accessPermissionListFormCache, requestUrl);
                } else {
                    // 当从缓存中取到系统中所有的的操作权限为空的时候,直接从MYSQL中查询用户的权限
                    List<AccessPermission> accessPermissionListFormMYSQL = getAccessPermissionListFormMYSQL();
                    setAccessPermissionListToCache(accessPermissionListFormMYSQL);
                    hasPermission = hasPermission(permissionFormCache, accessPermissionListFormMYSQL, requestUrl);
                }
            }
            //    2.1.1 有权限则放行到下一个Filter
            if (hasPermission) {
                chain.doFilter(request, response);
            } else {
                // 2.1.2   重定向到无权限页面友情提示页面
                response.sendRedirect(NO_PERMISSION);
            }
        }

    }

    /**
     * 获取用户的权限从MYSQL数据库里面
     *
     * @param username 用户名
     * @return 字符权限集合
     */
    private List<String> getPermissionFormMYSQL(String username) {
        Account account = accountMapper.selectByName(username);
        List<AccountRoles> accountRolesList = account.getAccountRolesList();
        //   2. 取出所有用户拥有的权限,放到HashSet里面去重
        List<String> stringPermissionList = new ArrayList<>();
        // 遍历所有的角色
        for (AccountRoles accountRoles : accountRolesList) {
            List<Permission> permissionList = accountRoles.getPermissionList();
            // 遍历当前循环中角色中所有的权限
            for (Permission permission : permissionList) {
                // 把权限实体转换为String权限,放入集合中去
                if (!stringPermissionList.contains(permission.getpermissionName())) {
                    String permissionName = permission.getpermissionName();
                    stringPermissionList.add(permissionName);
                }
            }
        }
        return stringPermissionList;
    }

    /**
     * 把权限放到缓存里面去
     *
     * @param permissionList 权限列表
     * @param username       用户名
     */
    private void setPermissionToCache(List<String> permissionList, String username) {
        Cache cache = new Cache();
        cache.setKey(PERMISSION_CACHE_PREFIX + username);
        cache.setValue(permissionList);
        CacheManager.putCache(PERMISSION_CACHE_PREFIX + username, cache);
    }

    /**
     * 获取用户的权限从缓存里面
     *
     * @param username 用户名
     * @return 字符权限集合
     */
    private List<String> getPermissionFormCache(String username) {
        Cache cache = CacheManager.getCache(PERMISSION_CACHE_PREFIX + username);
        if (cache != null) {
            return (List<String>) cache.getValue();
        }
        return Collections.emptyList();
    }

    /**
     * 把权限放到缓存里面去
     *
     * @param accessPermissionList 操作需要的权限列表
     */
    private void setAccessPermissionListToCache(List<AccessPermission> accessPermissionList) {
        Cache cache = new Cache();
        cache.setKey(ACCESS_PERMISSION_CACHE_PREFIX);
        cache.setValue(accessPermissionList);
        CacheManager.putCache(ACCESS_PERMISSION_CACHE_PREFIX, cache);
    }

    /**
     * 从MYSQL数据库中取得系统所有操作需要的权限
     *
     * @return 所有操作需要的权限
     */
    private List<AccessPermission> getAccessPermissionListFormCache() {
        Cache cache = CacheManager.getCache(ACCESS_PERMISSION_CACHE_PREFIX);
        if (cache != null) {
            return (List<AccessPermission>) cache.getValue();
        }
        return Collections.emptyList();
    }


    /**
     * 从MYSQL数据库中取得所有操作需要的权限
     *
     * @return 所有操作需要的权限
     */
    private List<AccessPermission> getAccessPermissionListFormMYSQL() {
        return accessPermissionService.selectAll();
    }


    /**
     * 验证用户是否有这个权限操作当前的URL地址.
     *
     * @param path 访问的URL地址.
     * @return 有访问这个URL地址的权限就返回true, 否则就返回false
     */
    private boolean hasPermission(List<String> permissionList, List<AccessPermission> accessPermissionList, String path) {
        String urlRequirePermission = null;
        boolean hasPermission;
        if (!permissionList.isEmpty()) {
            // 3.3 查询当前访问的URL需要什么权限
            for (AccessPermission accessPermission : accessPermissionList) {
                if (accessPermission.getAccessUrl().equals(path)) {
                    urlRequirePermission = accessPermission.getAccessPermission();
                    break;
                }
            }
            // 3.4 前面虽然已经判断了一次,但是有些URL也是可以匿名访问的
            if (urlRequirePermission != null) {
                //  3.4.3  循环匹配每一个权限实体里面的权限字符串跟请求的权限字符串是否相等,如果为true就直接跳出循环
                for (String permissionName : permissionList) {
                    hasPermission = permissionName.equals(urlRequirePermission);
                    if (hasPermission) {
                        return true;
                    }
                }
            }
            logger.info("这个页面不需要权限就可以访问");
            return true;
        }
        return false;
    }


    @Override
    public void destroy() {
        logger.warn("权限验证过滤器开始销毁");
    }

}





























