package ru.t1.java.demo.config.kafka;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.kafka.clients.CommonClientConfigs;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "t1-demo.kafka")
public class T1KafkaConfig {

    private List<String> bootstrapServers = new ArrayList<>(Collections.singletonList("localhost:9092"));
    private String clientId;
    private ListenerConfig listener;
    private Map<String, ProducerConfig> producer;
    private Map<String, ConsumerConfig> consumer;


    public Map<String, Object> buildProducerProperties(String key) {
        if (!producer.containsKey(key)) {
            return null;
        }

        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(producer.get(key).buildProperties());

        return properties;
    }

    public Map<String, Object> buildConsumerProperties(String key) {
        if (!consumer.containsKey(key)) {
            return null;
        }

        Map<String, Object> properties = buildCommonProperties();
        properties.putAll(consumer.get(key).buildProperties());

        return properties;
    }

    @NonNull
    private Map<String, Object> buildCommonProperties() {
        Map<String, Object> properties = new HashMap<>();

        Optional.ofNullable(bootstrapServers)
                .ifPresent(s -> properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, s));
        Optional.ofNullable(clientId)
                .ifPresent(s -> properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, s));

        return properties;
    }
}
