package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("transaction_id")
    private UUID transactionId;

    @JsonProperty("transaction_status")
    private String transactionStatus;

    private BigDecimal amount;

    @JsonProperty("create_time")
    private LocalDateTime createTime;

    protected String baseString() {
        return "accountId=" + accountId + ", transactionId=" + transactionId + ", transactionStatus=" + transactionStatus
                + ", createTime=" + createTime + ", amount=" + amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionDto that)) return false;
        return Objects.equals(accountId, that.accountId)
                && Objects.equals(transactionId, that.transactionId)
                && Objects.equals(transactionStatus, that.transactionStatus)
                && Objects.equals(amount, that.amount)
                && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, transactionId, transactionStatus, amount, createTime);
    }

    @Override
    public String toString() {
        return "TransactionDto [" + baseString() + "]";
    }

}
