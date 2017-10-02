package com.es.demo.common.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.es.demo.common.exception.CustomException;
import com.es.demo.common.model.ResponseResult;

@Aspect
@Component
public class ControllerHandler {

	private static final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);

	@Pointcut("execution(public com.es.demo.common.model.ResponseResult *(..))")
	public void pointCut() {

	}

	@Around(value = "pointCut()")
	public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
		long startTime = System.currentTimeMillis();
		ResponseResult<?> result;
		try {
			result = (ResponseResult<?>) pjp.proceed();
			logger.info(pjp.getSignature() + "use time:" + (System.currentTimeMillis() - startTime));
		} catch (Throwable e) {
			result = handlerException(pjp, e);
		}
		return result;
	}

	private ResponseResult<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
		ResponseResult<?> result = new ResponseResult();
		// 已知异常
		if (e instanceof CustomException) {
			result.setMsg(e.getLocalizedMessage());
			result.setCode(ResponseResult.FAIL);
		} else {
			logger.error(pjp.getSignature() + " error ", e);
			result.setMsg(e.toString());
			result.setCode(ResponseResult.FAIL);
			// 未知异常是应该重点关注的，这里可以做其他操作，如通知邮件，单独写到某个文件等等。
		}
		return result;
	}

}
