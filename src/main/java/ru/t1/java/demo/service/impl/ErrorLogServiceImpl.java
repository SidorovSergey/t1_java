package ru.t1.java.demo.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dao.DataSourceErrorLogDao;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.service.ErrorLogService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorLogServiceImpl implements ErrorLogService {

    private final DataSourceErrorLogDao errorLogDao;

    @Override
    public void logError(@NonNull String message, @NonNull String signature, @NonNull String stackTrace) {
        log.info("logError to: message=[{}], signature=[{}], stackTrace=[{}]", message, signature, stackTrace);

        var logError = errorLogDao.insert(
                new DataSourceErrorLog()
                        .setMessage(message)
                        .setSignature(signature)
                        .setStackTrace(stackTrace));

        log.info("logError from: id=[{}]", logError.getId());
    }

}
