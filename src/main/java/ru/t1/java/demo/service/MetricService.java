package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.MetricDto;
import ru.t1.java.demo.dto.MetricType;

public interface MetricService {

    String METRIC_NAME = "metric";

    void send(MetricType type, MetricDto metric);

}
