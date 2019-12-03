package com.valor.server.aop;

import com.valor.server.service.SysLogService;
import com.valor.server.service.UpmsStatService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kemp.Cheng
 * created on: 2019/11/11
 * Copyright Valoroso Ltd. (c) 2019.  All rights reserved.
 */
@Aspect
@Component
public class UpmsLogCallAspect {

    private final Logger logger = LoggerFactory.getLogger(UpmsLogCallAspect.class);

    @Autowired
    private UpmsStatService statService;

    @Autowired
    private SysLogService logService;

    @Pointcut("@annotation(com.valor.server.aop.UpmsLogCall)")
    public void excute() {
    }

    @Around("excute()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        long duration = -1L;
        Object result = null;
        Throwable throwableEx = null;
        try {
            long startTime = System.currentTimeMillis();
            result = joinPoint.proceed(joinPoint.getArgs());
            duration = System.currentTimeMillis() - startTime;
        } catch (Throwable throwable) {
            throwableEx = throwable;
            result = "Exception:" + throwable.getMessage();
        } finally {
            try {
                Signature apiSignature = joinPoint.getSignature();
                MethodSignature methodSignature = (MethodSignature) apiSignature;
                Method targetMethod = methodSignature.getMethod();
                if (targetMethod.isAnnotationPresent(UpmsLogCall.class)) {
                    UpmsLogCall logCall = targetMethod.getAnnotation(UpmsLogCall.class);
                    if (logCall.openLog()) {
                        String clientIp = request.getHeader("clientIp");
                        String reqUrl = request.getRequestURI();
                        String operateUser = request.getHeader("reqUserName");
                        List<Object> adArgs = new ArrayList();
                        for (Object arg : joinPoint.getArgs()) {
                            if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) {
                                adArgs.add(arg);
                            }
                        }
                        String reqDetail = adArgs.toString();
                        String respDetail = result == null ? "" : result.toString();
                        logService.logRecord(clientIp, reqUrl, operateUser, duration, reqDetail, respDetail);
                    }
                    if (logCall.openStat()) {
                        statService.put(apiSignature.toString(), duration);
                    }
                }
            } catch (Exception ex) {
                logger.error("Log Recording Error!", ex);
            } finally {
                if (throwableEx != null) {
                    throw throwableEx;
                } else {
                    return result;
                }
            }
        }
    }
}
