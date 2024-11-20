package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.MetricDto;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.service.ErrorLogService;

import static ru.t1.java.demo.dto.MetricType.DATA_SOURCE;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionAspect extends BaseAspect {

    private final KafkaProducer<MetricDto> kafkaProducer;
    private final ErrorLogService errorLogService;

    @AfterThrowing(pointcut = "@annotation(LogDataSourceError)", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) {
        log.debug("Error message interceptor started");

        MetricDto metric = getMetric(joinPoint);
        try {
            kafkaProducer.sendMessage(metric, DATA_SOURCE);
        } catch (Exception e) {
            log.error("Failed to send metric: metric=[{}]", metric, ex);

            saveException(joinPoint, ex);
        }

        log.debug("Error message interceptor finished");
    }

    private void saveException(JoinPoint joinPoint, Exception ex) {
        try {
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            String message = ex.getMessage();
            String signature = joinPoint.getSignature().toShortString();

            errorLogService.logError(message, signature, stackTrace);
        } catch (Exception e) {
            log.error("Failed to save exception", e);
        }
    }

}
