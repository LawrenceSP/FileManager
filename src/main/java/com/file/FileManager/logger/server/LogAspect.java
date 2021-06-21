package com.file.FileManager.logger.server;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

@Aspect
@Configuration
public class LogAspect {

	@Before("execution(* com.file.FileManager..*(..))")
	public void before(JoinPoint joinPoint) {
		LoggerFactory.getLogger(ClassUtils.getUserClass(joinPoint.getTarget().getClass())).info("start method:{}", joinPoint.getSignature().getName());
	}
	@After("execution(* com.file.FileManager..*(..))")
	public void after(JoinPoint joinPoint) {
		LoggerFactory.getLogger(ClassUtils.getUserClass(joinPoint.getTarget().getClass())).info("end of method:{}", joinPoint.getSignature().getName());
	}
}
