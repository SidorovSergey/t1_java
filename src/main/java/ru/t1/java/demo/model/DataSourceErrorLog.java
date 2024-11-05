package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "t1$error_log")
public class DataSourceErrorLog {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "signature", nullable = false)
    private String signature;

    @Column(name = "stack_trace", nullable = false)
    private String stackTrace;
}
