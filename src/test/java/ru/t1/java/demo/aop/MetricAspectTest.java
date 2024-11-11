package ru.t1.java.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;
import ru.t1.java.demo.service.MetricService;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetricAspectTest {

    @Mock
    private Logger logger;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MetricService metricService;

    @InjectMocks
    private MetricAspect metricAspect;

    @Test
    void timeMeasure() throws Throwable {
        // setup
        ReflectionTestUtils.setField(metricAspect, "limitTimeMs", -1);

        Method method = mock(Method.class);
        when(method.getName()).thenReturn("test");

        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(method);
        when(signature.getParameterNames()).thenReturn(new String[]{"id", "value"});

        when(joinPoint.getSignature()).thenReturn(signature);
        when(joinPoint.proceed()).thenReturn(null);

        // when
        metricAspect.timeMeasure(joinPoint);

        // then
        verify(joinPoint, times(1)).proceed();
        verify(logger, times(0)).info(anyString());
    }

}
