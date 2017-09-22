package com.suny.association.filter;


import com.suny.association.pojo.po.*;
import com.suny.association.service.interfaces.system.IAccessPermissionService;
import com.suny.association.service.interfaces.system.IPermissionAllotService;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Comments:   权限控制过滤器
 * Author:   孙建荣
 * Create Date: 2017/05/11 21:28
 */
public class PermissionFilter implements Filter {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PermissionFilter.class);
    private static final String PORTAL_LOGIN_URL = "/login.html";
    private static final String BACKEND_LOGIN_URL = "/backend/login.html";
    private static final String NO_PERMISSION = "/403.jsp";
    private IPermissionAllotService permissionAllotService;
    private IAccessPermissionService accessPermissionService;
    private HostHolder hostHolder;


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
        hostHolder = (HostHolder) context.getBean("hostHolder");
        logger.info("===============权限验证过滤器开始初始化============");
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 1.判断是否要继续执行下一个Filter,executeNextFilter值为false的话就直接放行到下一个Filter
        Boolean executeNextFilter = (Boolean) req.getAttribute("EXECUTE_NEXT_FILTER");
        if (!executeNextFilter) {
            logger.info("EXECUTE_NEXT_FILTER值为{},权限认证Filter放行", executeNextFilter);
            chain.doFilter(req, resp);
        } else {
            String reqURI = request.getRequestURI();
            //   【1】. 如果可以在本地线程变量里面取到Account信息,根据URL判断是否有对应的操作权限,否则就要求登录
            Account account = hostHolder.getAccount();
            //   【2】. 判断是否已经存在Account信息
            if (account != null) {
                // 2.1 判断是否有对应的权限
                boolean isPermission = isPermission(account, reqURI);
                //    2.1.1 有权限则放行到下一个Filter
                if (isPermission) {
                    logger.info("用户{}具有访问{}的权限,验证通过放行", account.getAccountName(), reqURI);
                    chain.doFilter(request, response);
                } else {
                    // 2.1.2   重定向到无权限页面友情提示页面
                    logger.info("用户{}不具有访问{}的权限,验证失败,重定向到{}页面", account.getAccountName(), reqURI, NO_PERMISSION);
                    response.sendRedirect(NO_PERMISSION);
                }
            } else {
                // 【3】. 没有取到Account信息,所以就直接重定向到登录页面
                logger.error("权限认证Filter中没有取到登录账号信息,直接发到登录页面");
                response.sendRedirect(request.getContextPath() + PORTAL_LOGIN_URL);
            }
        }

    }

    /**
     * 验证用户是否有这个权限操作当前的URL地址.
     *
     * @param account 用户账号.
     * @param path    访问的URL地址.
     * @return 有访问这个URL地址的权限就返回true, 否则就返回false
     */
    private boolean isPermission(Account account, String path) {
        //   1. 得到账号的角色,然后查询到用户对应的角色,    注意!这里使用的是单用户单角色
        Integer roleId = account.getAccountRoles().getRoleId();
        //   2. 构建一个有序的角色集合
        AtomicReference<HashSet<String>> permissions = new AtomicReference<>(new HashSet<>());
        List<PermissionAllot> permissionAllotList = permissionAllotService.queryByRoleId(roleId);
        //   3. 首先进行判断，防止角色没有权限导致数据下标溢出
        if (!permissionAllotList.isEmpty()) {
            // 3.1 这里得到的是账号-角色中间表的数据,所以得到的是一个大的集合,所以先取到第一个下标的数据,大的集合[下标]里面就是所有的权限数据
            List<Permission> permissionArrayList = permissionAllotList.get(0).getPermissionArrayList();
            // 3.2 把权限集合放到一个有序的集合里面去
            permissions.get().addAll(permissionArrayList.stream().map(Permission::getpermissionName).collect(Collectors.toList()));
            // 3.3 查询当前访问的URL需要什么权限
            AccessPermission accessPermission = accessPermissionService.queryByName(path);
            // 3.4 前面虽然已经判断了一次,但是有些URL也是可以匿名访问的
            if (accessPermission == null) {
                logger.info("这个页面不需要权限就可以访问");
                return true;
            } else {
                //  3.4.1 从权限实体中获取当前操作所需要得到权限字符串
                String urlPermission = accessPermission.getAccessPermission();
                //  3.4.2 定义一个标记,在循环中使用
                boolean hasPermission = false;
                //  3.4.3  循环匹配每一个权限实体里面的权限字符串跟请求的权限字符串是否相等,如果为true就直接跳出循环
                for (Permission per : permissionArrayList) {
                    hasPermission = per.getpermissionName().equals(urlPermission);
                    if (hasPermission) break;
                }
                //  3.4.4 有权限就直接放行,没有就返回false,有就返回true
                if (hasPermission) {
                    logger.info("访问当前页面需要【{}】用户{}有访问【{}】这个权限，放行", urlPermission, account.getAccountName(), urlPermission);
                    return true;
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





























