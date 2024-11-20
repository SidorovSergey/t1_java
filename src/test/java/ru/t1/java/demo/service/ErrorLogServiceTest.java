package ru.t1.java.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.t1.java.demo.dao.DataSourceErrorLogDao;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.service.impl.ErrorLogServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Import(ErrorLogServiceTest.ConfigurationTest.class)
@ContextConfiguration(classes = ErrorLogServiceImpl.class)
public class ErrorLogServiceTest {

    @MockBean
    private DataSourceErrorLogDao errorLogDao;

    @Autowired
    private ErrorLogService errorLogService;

    @TestConfiguration
    static class ConfigurationTest {
    }

    @Test
    void logErrorTest() {
        // setup
        String message = "test message error";
        String signature = "test signature error";
        String stackTrace = "test stack trace error";

        when(errorLogDao.insert(any()))
                .thenReturn(new DataSourceErrorLog()
                        .setId(1L)
                        .setMessage(message)
                        .setSignature(signature)
                        .setStackTrace(stackTrace));

        // when
        errorLogService.logError(message, signature, stackTrace);

        // then
        verify(errorLogDao, times(1)).insert(any());
    }
}
