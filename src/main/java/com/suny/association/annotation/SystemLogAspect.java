package com.suny.association.annotation;

import com.suny.association.common.RequestHolder;
import com.suny.association.entity.po.Account;
import com.suny.association.entity.po.Member;
import com.suny.association.entity.po.OperationLog;
import com.suny.association.service.interfaces.system.IOperationLogService;
import com.suny.association.utils.JackJsonUtil;
import com.suny.association.utils.WebUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * Comments:  系统操作日志切点类
 * @author :   孙建荣
 * Create Date: 2017/04/25 13:42
 */
@Aspect
@Component
public class SystemLogAspect {

    private final IOperationLogService operationLogService;

    /**
     *本地日志记录对象
     */
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    @Autowired
    public SystemLogAspect(IOperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     *service层切点
     */
    @Pointcut("@annotation(com.suny.association.annotation.SystemServiceLog)")
    public void serviceAspect() {

    }

    /**
     *controller层切点
     */
    @Pointcut("@annotation(com.suny.association.annotation.SystemControllerLog)")
    public void controllerAspect() {

    }

    /**
     * 前置通知  用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Account account= RequestHolder.getAccountHolder();
        try {
            /*控制台输出*/
            /*数据库日志，下面是把数据报存到数据库的的动作*/
            OperationLog operationLog = new OperationLog();
            String userAgent = request.getHeader("user-agent");
             /*  操作动作  */
            operationLog.setOperationMessage(getControllerMethodDescription(joinPoint));
            /* 操作者姓名，可选 */
            operationLog.setOperationMemberName(account.getAccountName());
             /*  操作浏览器  */
            operationLog.setOperationBrower(WebUtils.getBrowserInfo(userAgent));
             /*  操作系统 */
            operationLog.setOperationOsVersion(WebUtils.getClientOS(userAgent));
             /*  userAgent  */
            operationLog.setOperationUserAgent(userAgent);
             /*   请求的地址 */
            operationLog.setOperationRequestUrl(request.getRequestURI());
             /*  操作者账号 */
            operationLog.setOperationAccountId(account);
             /*  操作时间 : 数据库自动生成 */
             /*  操作状态  */
            operationLog.setOperationStatus(true);
             /*   操作ip*/
            String clientIpAdderess = WebUtils.getClientIpAdder(WebUtils.getHttpServletRequest());
            WebUtils.IpInfo ipInfo = WebUtils.getIpInfo(clientIpAdderess);
            if(ipInfo != null){
                operationLog.setOperationAddress(ipInfo.getCounty() + ipInfo.getArea() + ipInfo.getRegion() + ipInfo.getCity() + ipInfo.getIsp());
            }else{
                operationLog.setOperationAddress("未知地址");
            }
            operationLog.setOperationIp(clientIpAdderess);
            // 开始插入操作日志
            operationLogService.insert(operationLog);
        } catch (Exception e) {
            //记录本地异常日志
            logger.error("==前置通知异常==异常信息:{}", e.getMessage());
        }
    }


    /**
     * 业务逻辑层抛异常日志类
     *
     * @param joinPoint 切点
     * @param e         抛出异常的信息
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Account account= RequestHolder.getAccountHolder();
        //获取请求的ip
        String ip = WebUtils.getClientIpAdder(request);
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            StringBuilder paramsBuilder = new StringBuilder();
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                paramsBuilder.append(JackJsonUtil.objectToJson(joinPoint.getArgs()[i])).append(";");
            }
            params = paramsBuilder.toString();
        }
        try {
        /*==控制台的输出==*/
            /*数据库日志，下面是把数据报存到数据库的的动作*/
            OperationLog operationLog = new OperationLog();
            String userAgent = request.getHeader("user-agent");
             /*  操作动作  */
            operationLog.setOperationMessage(getControllerMethodDescription(joinPoint));
            /* 操作者姓名，可选 */
            operationLog.setOperationMemberName(account.getAccountName());
             /*  操作浏览器  */
            operationLog.setOperationBrower(WebUtils.getBrowserInfo(userAgent));
             /*  操作系统 */
            operationLog.setOperationOsVersion(WebUtils.getClientOS(userAgent));
             /*  userAgent  */
            operationLog.setOperationUserAgent(userAgent);
             /*   请求的地址 */
            operationLog.setOperationRequestUrl(request.getRequestURI());
             /*  操作者账号 */
            operationLog.setOperationAccountId(account);
             /*  操作时间 : 数据库自动生成 */
             /*  操作状态  */
            operationLog.setOperationStatus(false);
             /*   操作ip*/
            operationLog.setOperationIp(WebUtils.getClientIpAdder(request));
             /*  操作地址 */
            //noinspection ConstantConditions
            operationLog.setOperationAddress(WebUtils.getIpInfo(ip).toString());

            /*开始插入操作日志*/
            operationLogService.insert(operationLog);

            logger.info("===异常日志结束====");
        } catch (ClassNotFoundException ex) {
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}" + ex.getMessage());
        }
        /*=====记录本地异常日志=======*/
        logger.error("异常方法：{}异常代码:{}异常信息：{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);
    }


    /**
     * 获取注解中对方法的描述  用于拦截service层的异常
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws ClassNotFoundException 类不能找到异常
     */
    private static String getServiceMethodDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] classes = method.getParameterTypes();
                if (classes.length == arguments.length) {
                    description = method.getAnnotation(SystemServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }


    /**
     * 获取注解中对方法的描述
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws ClassNotFoundException 可能找不到类
     */
    private static String getControllerMethodDescription(JoinPoint joinPoint) throws ClassNotFoundException {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] classes = method.getParameterTypes();
                if (classes.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }


}































