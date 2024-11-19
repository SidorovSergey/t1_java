package ru.t1.java.demo.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class ConsumerConfig {

    private String topic;
    private String groupId;
    private Boolean enableAutoCommit;
    private String autoOffsetReset;
    private Integer maxPollRecords;
    private Integer maxPollIntervalMs;
    private Integer sessionTimeoutMs;
    private Integer maxPartitionFetchBytes;
    private String typeDto;
    private Class<?> keyDeserializer = StringDeserializer.class;
    private Class<?> valueDeserializer = StringDeserializer.class;

    public Map<String, Object> buildProperties() {
        Map<String, Object> properties = new HashMap<>();

        Optional.ofNullable(groupId)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, s));
        Optional.ofNullable(enableAutoCommit)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, s));
        Optional.ofNullable(autoOffsetReset)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, s));
        Optional.ofNullable(maxPollRecords)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, s));
        Optional.ofNullable(sessionTimeoutMs)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, s));
        Optional.ofNullable(maxPartitionFetchBytes)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, s));
        Optional.ofNullable(maxPollIntervalMs)
                .ifPresent(s -> properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, s));
        Optional.ofNullable(typeDto)
                .ifPresent(s -> {
                    properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, s);
                    properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
                    properties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
                });

        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        return properties;
    }
}
