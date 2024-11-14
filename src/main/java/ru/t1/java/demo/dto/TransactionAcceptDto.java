package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAcceptDto {

    @JsonProperty("client_id")
    private UUID clientId;

    @JsonProperty("account_id")
    private UUID accountId;

    @JsonProperty("transaction_id")
    private UUID transactionId;

    private Timestamp timestamp;

    @JsonProperty("transaction_amount")
    private BigDecimal transactionAmount;

    @JsonProperty("account_balance")
    private BigDecimal accountBalance;

    @Override
    public String toString() {
        return "TransactionAcceptDto [accountId=" + accountId + ", transactionId=" + transactionId + ", timestamp=" + timestamp +
                ", transactionAmount=" + transactionAmount + ", accountBalance=" + accountBalance + "]";
    }
}
