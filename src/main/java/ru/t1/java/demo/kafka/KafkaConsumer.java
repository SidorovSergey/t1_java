package ru.t1.java.demo.kafka;

import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

public interface KafkaConsumer<T> {

    String ACCOUNT_NAME = "account";
    String TRANSACTION_NAME = "transaction";
    String TRANSACTION_RESULT_NAME = "transaction_result";

    void listen(Acknowledgment ack, String key, List<T> messages);
}
