package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {

    @JsonProperty("account_id")
    private Long accountId;

    @JsonProperty("transaction_id")
    private UUID transactionId;

    private BigDecimal amount;

    @JsonProperty("create_time")
    private LocalDateTime createTime;

    protected String baseString() {
        return "accountId=" + accountId + ", transactionId=" + transactionId + ", createTime=" + createTime + ", amount=" + amount;
    }

    @Override
    public String toString() {
        return "TransactionDto [" + baseString() + "]";
    }

}
