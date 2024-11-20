package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "t1$account")
public class Account {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Enumerated(STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @Enumerated(STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "balance", precision = 12, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "frozen_amount", precision = 12, scale = 2)
    private BigDecimal frozenAmount;
}
