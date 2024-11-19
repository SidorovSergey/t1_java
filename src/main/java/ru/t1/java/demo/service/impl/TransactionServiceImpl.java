package ru.t1.java.demo.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
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

    @Metric
    @Override
    @LogDataSourceError
    public TransactionResDto getTransaction(@NonNull Long id) {
        log.info("to getTransaction: id=[{}]", id);

        TransactionResDto transaction = transactionMapper.toTransactionDto(transactionDao.findById(id));

        log.info("from getTransaction: transaction=[{}]", transaction);
        return transaction;
    }

    @Metric
    @Override
    @LogDataSourceError
    public List<TransactionResDto> getTransactions(@NonNull Long accountId) {
        log.info("to getTransactions: accountId=[{}]", accountId);

        List<TransactionResDto> transactions = transactionMapper.toTransactionDtos(transactionDao.findByAccountId(accountId));

        log.info("from getTransactions: List=[{}]", transactions);
        return transactions;
    }

    @Metric
    @Override
    @LogDataSourceError
    public TransactionResDto createTransaction(@NonNull TransactionDto transactionDto) {
        log.info("to createTransaction: transactionDto=[{}]", transactionDto);

        Transaction managedTransaction = transactionDao.insert(
                Optional.ofNullable(transactionMapper.toTransaction(transactionDto))
                        .orElseThrow(() -> new TransactionException("Invalid transaction data")));
        TransactionResDto transaction = transactionMapper.toTransactionDto(managedTransaction);

        log.info("from createTransaction: transaction=[{}]", transaction);
        return transaction;
    }

    @Metric
    @Override
    @LogDataSourceError
    public void deleteTransaction(@NonNull Long id) {
        log.info("to deleteTransaction: id=[{}]", id);

        transactionDao.deleteById(id);

        log.info("from deleteTransaction");
    }
}
