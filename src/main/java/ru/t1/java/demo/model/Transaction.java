package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "t1$transaction")
public class Transaction {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Enumerated(STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "created_at")
    private LocalDateTime createTime;

}
