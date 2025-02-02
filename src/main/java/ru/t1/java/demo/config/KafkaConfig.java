package ru.t1.java.demo.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.java.demo.config.kafka.T1KafkaConfig;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.TransactionDto;

import static ru.t1.java.demo.service.MetricService.METRIC_NAME;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final T1KafkaConfig config;

    // Producers
    @Bean(METRIC_NAME)
    public KafkaTemplate<String, Object> metricKafkaTemplate() {
        var t = producerFactory(METRIC_NAME);
        return new KafkaTemplate<>(t);
    }

    @NonNull
    private ProducerFactory<String, Object> producerFactory(@NonNull String name) {
        return new DefaultKafkaProducerFactory<>(config.buildProducerProperties(name));
    }

    // Consumers
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountDto> accountContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AccountDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(new DefaultKafkaConsumerFactory<>(config.buildConsumerProperties("account")), factory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionDto> transactionContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(new DefaultKafkaConsumerFactory<>(config.buildConsumerProperties("transaction")), factory);
        return factory;
    }

    private <T> void factoryBuilder(@NonNull ConsumerFactory<String, T> consumerFactory,
                                    @NonNull ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(config.getListener().isBatchListener());
        factory.setConcurrency(config.getListener().getConcurrency());
        factory.getContainerProperties().setAckMode(config.getListener().getAckMode());
        factory.getContainerProperties().setPollTimeout(config.getListener().getPollTimeout());
        factory.getContainerProperties().setMicrometerEnabled(config.getListener().isMicrometerEnabled());
        factory.setCommonErrorHandler(
                errorHandler(config.getListener().getErrorHandlerInterval(), config.getListener().getErrorHandlerMaxAttempts()));
    }

    @NonNull
    private CommonErrorHandler errorHandler(long interval, long maxAttempts) {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(interval, maxAttempts));

        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) ->
                log.error("Retry listeners: message=[{}], offset=[{}], deliveryAttempt=[{}]",
                        ex.getMessage(), record.offset(), deliveryAttempt));

        return handler;
    }
}
