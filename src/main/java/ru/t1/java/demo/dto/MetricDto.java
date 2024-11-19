package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricDto {

    @JsonProperty("method_name")
    private String methodName;

    @JsonProperty("method_params")
    private List<String> methodParams;

    @JsonProperty("elapsed_ms")
    private Long elapsedMs;

    @Override
    public String toString() {
        return "MetricDto [methodName=" + methodName + ", methodParams=" + methodParams + ", elapsedMs=" + elapsedMs + "]";
    }
}
