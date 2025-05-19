package com.devsco.moongch.common.logging;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Log4j2
@Component
public class LoggingAspect {

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void restController() {
  }

  @Before("restController() && execution(public * *(..))")
  public void logMethodEntry(JoinPoint jp) {
    String args = Arrays.deepToString(jp.getArgs());
    log.info("[{}] Enter {}.{}() with args={}",
      MDC.get(RequestLoggingFilter.REQUEST_ID),
      jp.getSignature().getDeclaringType().getSimpleName(),
      jp.getSignature().getName(),
      args
    );
  }

  @AfterReturning(value = "restController() && execution(public * *(..))", returning = "ret")
  public void logMethodExit(JoinPoint jp, Object ret) {
    log.info("[{}] Exit  {}.{}() return={}",
      MDC.get(RequestLoggingFilter.REQUEST_ID),
      jp.getSignature().getDeclaringType().getSimpleName(),
      jp.getSignature().getName(),
      ret
    );
  }

  @AfterThrowing(value = "restController() && execution(public * *(..))", throwing = "ex")
  public void logMethodException(JoinPoint jp, Throwable ex) {
    log.error("[{}] Exception in {}.{}(): {}",
      MDC.get(RequestLoggingFilter.REQUEST_ID),
      jp.getSignature().getDeclaringType().getSimpleName(),
      jp.getSignature().getName(),
      ex.getMessage(), ex
    );
  }
}
