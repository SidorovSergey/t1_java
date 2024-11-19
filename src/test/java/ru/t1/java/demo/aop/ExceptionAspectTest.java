package ru.t1.java.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import ru.t1.java.demo.service.ErrorLogService;
import ru.t1.java.demo.service.MetricService;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExceptionAspectTest {

    @Mock
    private Logger logger;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Exception ex;

    @Mock
    private MetricService metricService;

    @Mock
    private ErrorLogService errorLogService;

    @InjectMocks
    private ExceptionAspect exceptionAspect;

    @Test
    void handleException() {
        // setup
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("test");

        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(method);
        when(signature.getParameterNames()).thenReturn(new String[]{"id", "value"});

        when(joinPoint.getSignature()).thenReturn(signature);

        // when
        exceptionAspect.handleException(joinPoint, ex);

        // then
        verify(logger, times(0)).info(anyString());
    }

    @Test
    void handleExceptionNotSendToKafka() {
        // setup
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("test");

        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(method);
        when(signature.getParameterNames()).thenReturn(new String[]{"id", "value"});

        when(joinPoint.getSignature()).thenReturn(signature);
        doThrow(new RuntimeException("Error send to kafka")).when(metricService).send(any(), any());

        // when
        exceptionAspect.handleException(joinPoint, ex);

        // then
        verify(logger, times(0)).info(anyString());
    }

    @Test
    void handleExceptionNotSaveToDB() {
        // setup
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("test");

        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(method);
        when(signature.getParameterNames()).thenReturn(new String[]{"id", "value"});

        when(joinPoint.getSignature()).thenReturn(signature);
        doThrow(new RuntimeException("Error send to kafka")).when(metricService).send(any(), any());
        doThrow(new RuntimeException("Error save to DB")).when(errorLogService).logError(anyString(), anyString(), anyString());

        // when
        exceptionAspect.handleException(joinPoint, ex);

        // then
        verify(logger, times(0)).info(anyString());
    }
}
