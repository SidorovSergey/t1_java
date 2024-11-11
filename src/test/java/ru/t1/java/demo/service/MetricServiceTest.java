package ru.t1.java.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.t1.java.demo.config.kafka.ProducerConfig;
import ru.t1.java.demo.config.kafka.T1KafkaConfig;
import ru.t1.java.demo.dto.MetricDto;
import ru.t1.java.demo.dto.MetricType;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.service.impl.MetricServiceImpl;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Import(MetricServiceTest.ConfigurationTest.class)
@ContextConfiguration(classes = MetricService.class)
public class MetricServiceTest {

    private static final String TOPIC = "test_topic";

    @Autowired
    private KafkaProducer<MetricDto> kafkaProducer;

    @Autowired
    private MetricService metricService;

    @TestConfiguration
    static class ConfigurationTest {

        @Bean
        MetricService metricService() {
            return new MetricServiceImpl(
                    new T1KafkaConfig().setProducer(Map.of(MetricService.METRIC_NAME, new ProducerConfig().setTopic(TOPIC))),
                    kafkaProducer());
        }

        @Bean
        KafkaProducer<MetricDto> kafkaProducer() {
            return mock(KafkaProducer.class);
        }

    }

    @Test
    void sendTest() {
        // setup
        MetricType type = MetricType.METRICS;

        MetricDto metricDto = new MetricDto()
                .setMethodName("test_method")
                .setMethodParams(List.of("param1", "param2"))
                .setElapsedMs(123L);

        // when
        metricService.send(type, metricDto);

        // then
        verify(kafkaProducer, times(1))
                .sendMessage(TOPIC, type, metricDto);

    }

}
