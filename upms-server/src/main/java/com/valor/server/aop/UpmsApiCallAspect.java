package com.valor.server.aop;

import com.google.common.collect.Lists;
import com.valor.model.exception.APIException;
import com.valor.model.exception.UpmsHttpCode;
import com.valor.model.web.request.UpmsManageRequest;
import com.valor.model.web.response.UpmsResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class UpmsApiCallAspect {

	private final Logger logger = LoggerFactory.getLogger(UpmsApiCallAspect.class);

	@Pointcut("@annotation(com.valor.server.aop.UpmsApiCall)")
	public void excute() {
	}

	@Around("excute()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		UpmsResponse<?> result = null;
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		long duration = -1L;
		for (Object requestArg : args) {
			if (requestArg instanceof UpmsManageRequest) {
				UpmsManageRequest upmsManageRequest = (UpmsManageRequest) requestArg;
				if (request.getHeader("reqUserName") != null) {
					upmsManageRequest.setReqUserName(request.getHeader("reqUserName"));
				}
				if (request.getHeader("clientIp") != null) {
					upmsManageRequest.setClientIp(request.getHeader("clientIp"));
				}
				upmsManageRequest.setRequestTime(System.currentTimeMillis());
			}
		}
		try {
			long startTime = System.currentTimeMillis();
			result = (UpmsResponse<?>) joinPoint.proceed(args);
			duration = System.currentTimeMillis() - startTime;
		} catch (Exception e) {
			result = new UpmsResponse<>();
			if (e instanceof APIException) {
				APIException apiException = (APIException) e;
				result.setStatus(apiException.getRetCode() + "_" + apiException.getErrCode());
				result.setMsg(apiException.getMsg());
			} else {
				result.setStatus(UpmsHttpCode.SERVER_ERROR + "");
				result.setMsg(UpmsHttpCode.SERVER_ERROR_MSG);
			}
			logger.error("API error:" + joinPoint.getSignature() + ", params：" + Lists.newArrayList(args), e);
		}
		result.setServerId("upms valor");
		result.setTimeMs(duration);
		return result;
	}
}
