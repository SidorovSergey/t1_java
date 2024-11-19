package ru.t1.java.demo.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class ProducerConfig {
    private String topic;
    private String acks;
    private Integer retries;
    private Long retryBackoffMs;
    private Boolean enableIdempotence;
    private Class<?> keySerializer = StringSerializer.class;
    private Class<?> valueSerializer = StringSerializer.class;

    public Map<String, Object> buildProperties() {
        Map<String, Object> properties = new HashMap<>();

        Optional.ofNullable(acks)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, s));
        Optional.ofNullable(retries)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, s));
        Optional.ofNullable(retryBackoffMs)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.producer.ProducerConfig.RETRY_BACKOFF_MS_CONFIG, s));
        Optional.ofNullable(enableIdempotence)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, s));
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        properties.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

        return properties;
    }
}
