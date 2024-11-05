package ru.t1.java.demo.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountDao {

    private final AccountRepository accountRepository;

    @NonNull
    public Account findById(@NonNull Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountException("Account not found by id=" + id));
    }

    @NonNull
    public List<Account> findByClientId(@NonNull Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    @NonNull
    @Transactional
    public Account insert(@NonNull Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    public void deleteById(@NonNull Long id) {
        accountRepository.deleteById(id);
    }

}
