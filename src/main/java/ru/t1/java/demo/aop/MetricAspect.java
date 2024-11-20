package ru.t1.java.demo.aop;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.MetricDto;
import ru.t1.java.demo.kafka.KafkaProducer;

import static ru.t1.java.demo.dto.MetricType.METRICS;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect extends BaseAspect {

    @Value("${t1-demo.metric.limit-time-ms:100}")
    private long limitTimeMs;

    private final KafkaProducer<MetricDto> kafkaProducer;

    @Around("@annotation(Metric)")
    public Object timeMeasure(ProceedingJoinPoint joinPoint) throws Throwable {
        MetricDto metric = getMetric(joinPoint);
        long start = System.currentTimeMillis();
        long elapsedMs;
        Object result;

        try {
            result = joinPoint.proceed();
        } finally {
            elapsedMs = System.currentTimeMillis() - start;
            log.info("{} elapsed time: elapsedMs=[{}]", metric.getMethodName(), elapsedMs);
        }

        if (elapsedMs > limitTimeMs) {
            sendMetric(metric.setElapsedMs(elapsedMs));
        }

        return result;
    }

    private void sendMetric(@NonNull MetricDto metric) {
        try {
            kafkaProducer.sendMessage(metric, METRICS);
        } catch (Exception ex) {
            log.error("Failed to send metric: metric=[{}]", metric, ex);
        }
    }
}
