package ru.t1.java.demo.aop;

import lombok.NonNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import ru.t1.java.demo.dto.MetricDto;

import java.util.Arrays;

abstract class BaseAspect {

    @NonNull
    protected MetricDto getMetric(@NonNull JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return new MetricDto()
                .setMethodName(signature.getMethod().getName())
                .setMethodParams(Arrays.stream(signature.getParameterNames()).toList());
    }

}
