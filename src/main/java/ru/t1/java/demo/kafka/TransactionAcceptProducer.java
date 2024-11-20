package ru.t1.java.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.config.kafka.T1KafkaConfig;
import ru.t1.java.demo.dto.TransactionAcceptDto;

@Slf4j
@Component
public class TransactionAcceptProducer extends BaseKafkaProducer<TransactionAcceptDto> {

    protected TransactionAcceptProducer(T1KafkaConfig config,
                                        @Qualifier(TRANSACTION_ACCEPT_NAME) KafkaTemplate<String, Object> kafka) {
        super(config.getProducers().get(TRANSACTION_ACCEPT_NAME).getTopic(), kafka);
    }

}
