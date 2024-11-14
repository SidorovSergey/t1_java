package ru.t1.java.demo.kafka;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import ru.t1.java.demo.dto.MetricType;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
abstract class BaseKafkaProducer<T> implements KafkaProducer<T> {

    private final String topic;
    protected final KafkaTemplate<String, Object> kafka;

    protected BaseKafkaProducer(String topic, KafkaTemplate<String, Object> kafka) {
        this.topic = topic;
        this.kafka = kafka;
    }

    @Override
    public void sendMessage(@NonNull T message) {
        sendMessage(message, null);
    }

    @Override
    public void sendMessage(@NonNull T message, @Nullable MetricType type) {
        log.info("to sendMessage: topic=[{}], message=[{}], type=[{}]", topic, message, type);

        try {
            ProducerRecord record = getRecord(topic, message, type);
            CompletableFuture<SendResult<String, T>> future =  kafka.send(record);
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Message not sent: exceptionMsg=[{}]", ex.getMessage(), ex);
                } else {
                    String key = result.getProducerRecord().key();
                    long offset = result.getRecordMetadata().offset();
                    log.info("Message sent successfully: key=[{}], offset=[{}]", key, offset);
                }
            });
        } catch (Exception ex) {
            log.error("Unable to send message: exceptionMsg=[{}]", ex.getMessage(), ex);
        }

        log.info("from sendMessage");
    }

    @NonNull
    private ProducerRecord<String, T> getRecord(@NonNull String topic,
                                                @NonNull T message,
                                                @Nullable MetricType type) {
        ProducerRecord<String, T> record = new ProducerRecord<>(topic, generateKey(), message);
        Optional.ofNullable(type)
                .ifPresent(metricType -> record.headers().add(new RecordHeader("error_type", metricType.name().getBytes())));
        return record;
    }

    @NonNull
    private String generateKey() {
        return UUID.randomUUID().toString();
    }
}
