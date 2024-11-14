package ru.t1.java.demo.kafka;

import ru.t1.java.demo.dto.MetricType;

public interface KafkaProducer<T> {

    String TRANSACTION_ACCEPT_NAME = "transaction_accept";
    String METRIC_NAME = "metric";

    void sendMessage(T value);

    void sendMessage(T value, MetricType type);

}
