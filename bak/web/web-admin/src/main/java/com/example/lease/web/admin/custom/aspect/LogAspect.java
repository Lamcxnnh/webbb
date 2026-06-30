package com.example.lease.web.admin.custom.aspect;

import com.example.lease.common.annotation.LogSys;
import com.example.lease.common.login.LoginUser;
import com.example.lease.common.login.LoginUserHolder;
import com.example.lease.model.entity.SysLog;
import com.example.lease.web.admin.service.AsyncLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 操作日志 AOP 切面
 * <p>
 * 拦截所有被 @LogSys 注解标记的方法，收集请求信息、方法信息、
 * 执行结果等，并通过异步服务将日志保存到数据库
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private AsyncLogService asyncLogService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 切点：匹配所有被 @LogSys 注解标记的方法
     */
    @Pointcut("@annotation(com.example.lease.common.annotation.LogSys)")
    public void logPointcut() {
    }

    /**
     * 环绕通知：拦截目标方法，记录操作日志
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogSys logSys = method.getAnnotation(LogSys.class);

        // 构建日志实体
        SysLog sysLog = new SysLog();
        sysLog.setLogType(logSys.logType().getCode());
        sysLog.setOperation(logSys.value());
        sysLog.setMethodName(signature.getDeclaringTypeName() + "." + method.getName() + "()");
        sysLog.setCreateTime(new Date());

        // 获取请求信息
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                sysLog.setRequestUrl(request.getRequestURI());
                sysLog.setIpAddress(getClientIp(request));
            }
        } catch (Exception e) {
            log.warn("获取请求信息失败: {}", e.getMessage());
        }

        // 获取请求参数（排除 Request/Response 等不可序列化对象）
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                sysLog.setRequestParams(OBJECT_MAPPER.writeValueAsString(args));
            }
        } catch (Exception e) {
            log.warn("序列化请求参数失败: {}", e.getMessage());
        }

        // 获取当前操作人
        try {
            LoginUser loginUser = LoginUserHolder.getLoginUser();
            if (loginUser != null) {
                sysLog.setOperator(loginUser.getUsername());
            }
        } catch (Exception e) {
            // 未登录状态下忽略
        }

        // 执行目标方法
        Object result = null;
        try {
            result = joinPoint.proceed();
            // 记录成功结果，限制长度防止字段溢出
            try {
                if (result != null) {
                    String resultStr = OBJECT_MAPPER.writeValueAsString(result);
                    if (resultStr.length() > 2000) {
                        resultStr = resultStr.substring(0, 2000) + "...";
                    }
                    sysLog.setResult(resultStr);
                }
            } catch (Exception e) {
                log.warn("序列化返回结果失败: {}", e.getMessage());
            }
        } catch (Throwable e) {
            sysLog.setResult("异常: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            // 异常时也保存日志
            asyncSaveLog(sysLog);
            throw e;
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("[操作日志] {} | {} | 耗时: {}ms | IP: {} | 操作人: {}",
                logSys.logType().getName(), logSys.value(),
                elapsedTime, sysLog.getIpAddress(), sysLog.getOperator());

        // 异步保存日志
        asyncSaveLog(sysLog);

        return result;
    }

    /**
     * 异步保存日志，发生异常时仅记录不抛出，避免影响主流程
     */
    private void asyncSaveLog(SysLog sysLog) {
        try {
            asyncLogService.saveLog(sysLog);
        } catch (Exception e) {
            log.error("异步保存日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取客户端真实 IP 地址（处理代理场景）
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理取第一个有效 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
