package ru.t1.java.demo.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.config.kafka.T1KafkaConfig;
import ru.t1.java.demo.dto.MetricDto;
import ru.t1.java.demo.dto.MetricType;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.service.MetricService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

    private final String topic;
    private final KafkaProducer<MetricDto> kafkaProducer;

    @Autowired
    public MetricServiceImpl(T1KafkaConfig config,
                             KafkaProducer<MetricDto> kafkaProducer) {
        this.topic = config.getProducer().get(METRIC_NAME).getTopic();
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void send(@NonNull MetricType type, @NonNull MetricDto metric) {
        log.info("to send: type=[{}] metric=[{}]", type, metric);

        kafkaProducer.sendMessage(topic, type, metric);

        log.info("from send");
    }
}
