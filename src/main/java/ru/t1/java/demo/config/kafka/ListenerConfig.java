package ru.t1.java.demo.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.listener.ContainerProperties;

@Getter
@Setter
public class ListenerConfig {

    private ContainerProperties.AckMode ackMode;
    private long pollTimeout = 5000L;
    private int concurrency = 1;
    private boolean micrometerEnabled;
    private boolean batchListener;
    private ErrorHandler errorHandler;

    public long getErrorHandlerInterval() {
        return errorHandler.getInterval();
    }

    public long getErrorHandlerMaxAttempts() {
        return errorHandler.getMaxAttempts();
    }

    @Getter
    @Setter
    private static class ErrorHandler {
        private long interval = 5000L;
        private long maxAttempts = Long.MAX_VALUE;
    }
}
