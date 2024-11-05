package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("account_type")
    private String accountType;

    private BigDecimal balance;

    protected String baseString() {
        return "clientId=" + clientId + ", accountType=" + accountType + ", balance=" + balance;
    }

    @Override
    public String toString() {
        return "AccountDto [" + baseString() + "]";
    }
}
