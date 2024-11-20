package ru.t1.java.demo.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
import ru.t1.java.demo.aop.Metric;
import ru.t1.java.demo.dao.AccountDao;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.AccountResDto;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountDao accountDao;

    @Metric
    @Override
    @LogDataSourceError
    public AccountResDto getAccount(@NonNull Long id) {
        log.info("to getAccount: id=[{}]", id);

        AccountResDto account = accountMapper.toAccountResDto(accountDao.findById(id));

        log.info("from getAccount: account=[{}]", account);
        return account;
    }

    @Metric
    @Override
    @LogDataSourceError
    public List<AccountResDto> getAccounts(@NonNull Long clientId) {
        log.info("to getAccounts: clientId=[{}]", clientId);

        List<AccountResDto> accounts = accountMapper.toAccountDtos(accountDao.findByClientId(clientId));

        log.info("from getAccounts: List=[{}]", accounts);
        return accounts;
    }

    @Metric
    @Override
    @LogDataSourceError
    public AccountResDto createAccount(@NonNull AccountDto accountDto) {
        log.info("to createAccount: accountDto=[{}]", accountDto);

        Account managedAccount = accountDao.insert(
                Optional.ofNullable(accountMapper.toAccount(accountDto))
                        .orElseThrow(() -> new AccountException("Invalid account data")));
        AccountResDto account = accountMapper.toAccountResDto(managedAccount);

        log.info("from createAccount: account=[{}]", account);
        return account;
    }

    @Metric
    @Override
    @LogDataSourceError
    public void deleteAccount(@NonNull Long id) {
        log.info("to deleteAccount: id=[{}]", id);

        accountDao.deleteById(id);

        log.info("from deleteAccount");
    }
}
