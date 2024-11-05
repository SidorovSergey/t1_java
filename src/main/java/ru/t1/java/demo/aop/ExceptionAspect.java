package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.service.ErrorLogService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionAspect {

    private final ErrorLogService errorLogService;

    @AfterThrowing(pointcut = "@annotation(LogDataSourceError)", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) {
        log.debug("Error message interceptor started");

        String stackTrace = ExceptionUtils.getStackTrace(ex);
        String message = ex.getMessage();
        String signature = joinPoint.getSignature().toShortString();

        errorLogService.logError(message, signature, stackTrace);

        log.debug("Error message interceptor finished");
    }

}
