package ru.t1.java.demo.kafka;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.MetricDto;
import ru.t1.java.demo.dto.MetricType;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricProducer implements KafkaProducer<MetricDto> {

    private final KafkaTemplate<String, Object> kafka;

    @Override
    public void sendMessage(@NonNull String topic,
                            @NonNull MetricType type,
                            @NonNull MetricDto message) {
        log.info("to sendMessage: topic=[{}], type=[{}], message=[{}]", topic, type, message);
        try {
            ProducerRecord record = getRecord(topic, type, message);
            CompletableFuture<SendResult<String, MetricDto>> future =  kafka.send(record);
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
    private ProducerRecord<String, MetricDto> getRecord(@NonNull String topic,
                                                        @NonNull MetricType type,
                                                        @NonNull MetricDto value) {
        ProducerRecord<String, MetricDto> record = new ProducerRecord<>(topic, generateKey(), value);
        record.headers().add(new RecordHeader("error_type", type.name().getBytes()));
        return record;
    }

    @NonNull
    private String generateKey() {
        return UUID.randomUUID().toString();
    }
}
