package ru.t1.java.demo.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.dao.TransactionDao;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResDto;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionDao transactionDao;

    @Override
    @LogDataSourceError
    public TransactionResDto getTransaction(@NonNull Long id) {
        log.info("getTransaction to: id=[{}]", id);

        TransactionResDto transaction = transactionMapper.toTransactionDto(transactionDao.findById(id));

        log.info("getTransaction from: transaction=[{}]", transaction);
        return transaction;
    }

    @Override
    @LogDataSourceError
    public List<TransactionResDto> getTransactions(@NonNull Long accountId) {
        log.info("getTransactions to: accountId=[{}]", accountId);

        List<TransactionResDto> transactions = transactionMapper.toTransactionDtos(transactionDao.findByAccountId(accountId));

        log.info("getTransactions from: List=[{}]", transactions);
        return transactions;
    }

    @Override
    @LogDataSourceError
    public TransactionResDto createTransaction(@NonNull TransactionDto transactionDto) {
        log.info("createTransaction to: transactionDto=[{}]", transactionDto);

        Transaction managedTransaction = transactionDao.insert(
                Optional.ofNullable(transactionMapper.toTransaction(transactionDto))
                        .orElseThrow(() -> new TransactionException("Invalid transaction data")));
        TransactionResDto transaction = transactionMapper.toTransactionDto(managedTransaction);

        log.info("createTransaction from: transaction=[{}]", transaction);
        return transaction;
    }

    @Override
    @LogDataSourceError
    public void deleteTransaction(@NonNull Long id) {
        log.info("deleteTransaction to: id=[{}]", id);

        transactionDao.deleteById(id);

        log.info("deleteTransaction from");
    }
}
