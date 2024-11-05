package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResDto;

import java.util.List;

public interface TransactionService {

    TransactionResDto getTransaction(Long id);

    List<TransactionResDto> getTransactions(Long accountId);

    TransactionResDto createTransaction(TransactionDto transactionDto);

    void deleteTransaction(Long id);
}
