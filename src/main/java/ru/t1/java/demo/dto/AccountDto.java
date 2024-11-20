package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {

    @JsonProperty("client_id")
    private Long clientId;

    @JsonProperty("account_id")
    private UUID accountId;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("account_status")
    private String accountStatus;

    private BigDecimal balance;

    protected String baseString() {
        return "clientId=" + clientId + ", accountId=" + accountId + ", accountType=" + accountType + ", accountStatus="
                + accountStatus + ", balance=" + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDto that)) return false;
        return Objects.equals(clientId, that.clientId)
                && Objects.equals(accountId, that.accountId)
                && Objects.equals(accountType, that.accountType)
                && Objects.equals(accountStatus, that.accountStatus)
                && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, accountId, accountType, accountStatus, balance);
    }

    @Override
    public String toString() {
        return "AccountDto [" + baseString() + "]";
    }
}
