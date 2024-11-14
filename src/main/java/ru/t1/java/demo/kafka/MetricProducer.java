package ru.t1.java.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.config.kafka.T1KafkaConfig;
import ru.t1.java.demo.dto.MetricDto;

@Slf4j
@Component
public class MetricProducer extends BaseKafkaProducer<MetricDto> {

    public MetricProducer(T1KafkaConfig config, @Qualifier(METRIC_NAME) KafkaTemplate<String, Object> kafka) {
        super(config.getProducers().get(METRIC_NAME).getTopic(), kafka);
    }
}
