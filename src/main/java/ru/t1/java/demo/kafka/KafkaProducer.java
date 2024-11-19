package ru.t1.java.demo.kafka;

import ru.t1.java.demo.dto.MetricType;

public interface KafkaProducer<T> {

    void sendMessage(String topic, MetricType type, T value);

}
