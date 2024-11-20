package ru.t1.java.demo.service.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dao.AccountDao;
import ru.t1.java.demo.dao.ClientDao;
import ru.t1.java.demo.dao.TransactionDao;
import ru.t1.java.demo.dao.TxDao;
import ru.t1.java.demo.dto.TransactionAcceptDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResultDto;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.kafka.KafkaProducer;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.util.TransactionMapper;

import java.sql.Timestamp;
import java.util.Optional;

import static ru.t1.java.demo.model.AccountStatus.OPEN;
import static ru.t1.java.demo.model.TransactionStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionHandler {

    private final TxDao txDao;
    private final ClientDao clientDao;
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;
    private final TransactionMapper transactionMapper;
    private final KafkaProducer<TransactionAcceptDto> transactionAcceptProducer;

    @NonNull
    public Transaction request(@NonNull TransactionDto transactionDto) {
        log.info("to request: transactionDto=[{}]", transactionDto);

        Transaction transaction = Optional.ofNullable(transactionMapper.toTransaction(transactionDto))
                .orElseThrow(() -> new TransactionException("Invalid transaction data"));
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

        log.info("from request: transaction=[{}]", transaction);
        return transaction;
    }

    @NonNull
    public Transaction accept(@NonNull TransactionResultDto transactionResultDto) {
        log.info("to accept: transactionResultDto=[{}]", transactionResultDto);

        Transaction transaction = txDao.requiresNew(() ->  {
            Transaction originalTransaction = transactionDao.findByTransactionId(transactionResultDto.getTransactionId());
            Account account = accountDao.findByAccountId(transactionResultDto.getAccountId());

            account.setBalance(account.getBalance().subtract(originalTransaction.getAmount()));
            return originalTransaction.setTransactionStatus(ACCEPTED);
        });

        log.info("from accept: transaction=[{}]", transaction);
        return transaction;
    }

    @NonNull
    public Transaction reject(@NonNull TransactionResultDto transactionResultDto) {
        log.info("to reject: transactionResultDto=[{}]", transactionResultDto);

        Transaction transaction = txDao.requiresNew(() ->
                transactionDao.findByTransactionId(transactionResultDto.getTransactionId())
                        .setTransactionStatus(REJECTED));

        log.info("from reject: transaction=[{}]", transaction);
        return transaction;
    }

    @NonNull
    public Transaction block(@NonNull TransactionResultDto transactionResultDto) {
        log.info("to block: transactionResultDto=[{}]", transactionResultDto);

        Transaction transaction = txDao.requiresNew(() -> {
            Transaction originalTransaction = transactionDao.findByTransactionId(transactionResultDto.getTransactionId());
            Account account = accountDao.findByAccountId(transactionResultDto.getAccountId());

            account.setBalance(account.getBalance().subtract(originalTransaction.getAmount()))
                    .setFrozenAmount(originalTransaction.getAmount())
                    .setAccountStatus(AccountStatus.BLOCKED);

            return originalTransaction.setTransactionStatus(BLOCKED);
        });

        log.info("from block: transaction=[{}]", transaction);
        return transaction;
    }
}
