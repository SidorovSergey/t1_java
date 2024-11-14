package ru.t1.java.demo.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.dao.AccountDao;
import ru.t1.java.demo.dao.ClientDao;
import ru.t1.java.demo.dao.TransactionDao;
import ru.t1.java.demo.dto.TransactionAcceptDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResDto;
import ru.t1.java.demo.dto.TransactionResultDto;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.service.handler.TransactionResultHandler;
import ru.t1.java.demo.util.TransactionMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static ru.t1.java.demo.model.AccountStatus.OPEN;
import static ru.t1.java.demo.model.TransactionStatus.REQUESTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountDao accountDao;
    private final ClientDao clientDao;
    private final TransactionMapper transactionMapper;
    private final TransactionDao transactionDao;
    private final TransactionResultHandler transactionResultHandler;
    private final KafkaProducer<TransactionAcceptDto> transactionAcceptProducer;

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

        Transaction managedTransaction = transactionDao.insert(getTransactionFromDto(transactionDto));
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

    @Metric
    @Override
    @LogDataSourceError
    public void handleTransaction(@NonNull String key, @NonNull TransactionDto transactionDto) {
        log.info("to handleTransaction: key=[{}], transactionDto=[{}]", key, transactionDto);

        try {
            Transaction transaction = getTransactionFromDto(transactionDto);
            Account account = accountDao.findById(transaction.getAccountId());

            if (OPEN == account.getAccountStatus()) {
                transactionDao.insert(transaction
                        .setTransactionStatus(REQUESTED)
                        .setTimestamp(new Timestamp(System.currentTimeMillis())));

                account.setBalance(account.getBalance().subtract(transaction.getAmount()));

                transactionAcceptProducer.sendMessage(
                        transactionMapper.toTransactionAcceptDto(clientDao.findById(account.getClientId()), account, transaction));
            } else {
                log.warn("Account is not open: account=[{}]", account);
            }

        } catch (Exception ex) {
            log.error("Fail handle transaction: key=[{}], transactionDto=[{}]", key, transactionDto, ex);
        }

        log.info("from handleTransaction: key=[{}]", key);
    }

    @Metric
    @Override
    @LogDataSourceError
    public void handleTransactionResult(@NonNull String key, @NonNull TransactionResultDto transactionResultDto) {
        log.info("to handleTransactionResult: key=[{}], transactionResultDto=[{}]", key, transactionResultDto);

        try {
            Transaction transaction = switch (transactionResultDto.getStatus()) {
                case ACCEPTED -> transactionResultHandler.accept(transactionResultDto);
                case REJECTED -> transactionResultHandler.reject(transactionResultDto);
                case BLOCKED -> transactionResultHandler.block(transactionResultDto);
                default -> {
                    log.warn("Unexpected status: status=[{}]", transactionResultDto.getStatus());
                    yield null;
                }};

            log.debug("Transaction handled: transaction=[{}]", transaction);
        } catch (Exception ex) {
            log.error("Fail handle transaction result: key=[{}], transactionDto=[{}]", key, transactionResultDto, ex);
        }

        log.info("from handleTransactionResult: key=[{}]", key);
    }

    @NonNull
    private Transaction getTransactionFromDto(@NonNull TransactionDto transactionDto) {
        return Optional.ofNullable(transactionMapper.toTransaction(transactionDto))
                .orElseThrow(() -> new TransactionException("Invalid transaction data"));
    }
}
