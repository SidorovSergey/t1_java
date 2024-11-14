package ru.t1.java.demo.util;

import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;
import ru.t1.java.demo.dto.TransactionAcceptDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class TransactionMapper {

    @Nullable
    public TransactionResDto toTransactionDto(@Nullable Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return (TransactionResDto) new TransactionResDto()
                .setId(transaction.getId())
                .setAccountId(transaction.getAccountId())
                .setAmount(transaction.getAmount())
                .setCreateTime(transaction.getCreateTime());
    }

    @Nullable
    public List<TransactionResDto> toTransactionDtos(@Nullable List<Transaction> transactions) {
        if (isNull(transactions)) {
            return null;
        }

        List<TransactionResDto> dtos = new ArrayList<>();

        transactions.forEach(transaction ->
                Optional.ofNullable(toTransactionDto(transaction))
                        .ifPresent(dtos::add));

        return dtos;
    }

    @Nullable
    public Transaction toTransaction(@Nullable TransactionDto transactionDto) {
        if (isNull(transactionDto)) {
            return null;
        }
        return new Transaction()
                .setAccountId(transactionDto.getAccountId())
                .setTransactionId(transactionDto.getTransactionId())
                .setAmount(transactionDto.getAmount())
                .setCreateTime(transactionDto.getCreateTime());
    }

    public TransactionAcceptDto toTransactionAcceptDto(@NonNull Client client,
                                                       @NonNull Account account,
                                                       @NonNull Transaction transaction) {
        return new TransactionAcceptDto()
                .setClientId(client.getClientId())
                .setAccountId(account.getAccountId())
                .setTransactionId(transaction.getTransactionId())
                .setTimestamp(transaction.getTimestamp())
                .setTransactionAmount(transaction.getAmount())
                .setAccountBalance(account.getBalance());
    }

}
