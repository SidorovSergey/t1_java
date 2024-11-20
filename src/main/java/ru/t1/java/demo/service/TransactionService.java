package ru.t1.java.demo.service;

import lombok.NonNull;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResDto;
import ru.t1.java.demo.dto.TransactionResultDto;

import java.util.List;

public interface TransactionService {

    TransactionResDto getTransaction(Long id);

    List<TransactionResDto> getTransactions(Long accountId);

    TransactionResDto createTransaction(TransactionDto transactionDto);

    void deleteTransaction(Long id);

    void handleTransaction(@NonNull String key, @NonNull TransactionDto transaction);

    void handleTransactionResult(@NonNull String key, @NonNull TransactionResultDto transactionResult);
}
