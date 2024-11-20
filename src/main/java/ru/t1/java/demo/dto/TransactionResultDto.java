package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.t1.java.demo.model.TransactionStatus;

import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResultDto {

    @JsonProperty("account_id")
    private UUID accountId;

    @JsonProperty("transaction_id")
    private UUID transactionId;

    @JsonProperty("status")
    private TransactionStatus status;

    @Override
    public String toString() {
        return "TransactionResultDto [accountId=" + accountId + ", transactionId=" + transactionId + ", status=" + status + "]";
    }
}
