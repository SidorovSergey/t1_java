package ru.t1.java.demo.service.handler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dao.AccountDao;
import ru.t1.java.demo.dao.TransactionDao;
import ru.t1.java.demo.dao.TxDao;
import ru.t1.java.demo.dto.TransactionResultDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.Transaction;

import static ru.t1.java.demo.model.TransactionStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionResultHandler {

    private final TxDao txDao;
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

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
