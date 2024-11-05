package ru.t1.java.demo.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.exception.TransactionException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionDao {

    private final TransactionRepository transactionRepository;

    @NonNull
    public Transaction findById(@NonNull Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionException("Transaction not found by id=" + id));
    }

    @NonNull
    public List<Transaction> findByAccountId(@NonNull Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @NonNull
    @Transactional
    public Transaction insert(@NonNull Transaction account) {
        return transactionRepository.save(account);
    }

    @Transactional
    public void deleteById(@NonNull Long id) {
        transactionRepository.deleteById(id);
    }
}
