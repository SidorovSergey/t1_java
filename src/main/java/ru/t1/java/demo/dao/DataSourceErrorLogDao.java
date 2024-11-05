package ru.t1.java.demo.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceErrorLogDao {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    @NonNull
    @Transactional
    public DataSourceErrorLog insert(@NonNull DataSourceErrorLog errorLog) {
        return dataSourceErrorLogRepository.save(errorLog);
    }

}
