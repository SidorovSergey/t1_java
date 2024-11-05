package ru.t1.java.demo.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.LogDataSourceError;
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

    @Override
    @LogDataSourceError
    public AccountResDto getAccount(@NonNull Long id) {
        log.info("getAccount to: id=[{}]", id);

        AccountResDto account = accountMapper.toAccountDto(accountDao.findById(id));

        log.info("getAccount from: account=[{}]", account);
        return account;
    }

    @Override
    @LogDataSourceError
    public List<AccountResDto> getAccounts(@NonNull Long clientId) {
        log.info("getAccounts to: clientId=[{}]", clientId);

        List<AccountResDto> accounts = accountMapper.toAccountDtos(accountDao.findByClientId(clientId));

        log.info("getAccounts from: List=[{}]", accounts);
        return accounts;
    }

    @Override
    @LogDataSourceError
    public AccountResDto createAccount(@NonNull AccountDto accountDto) {
        log.info("createAccount to: accountDto=[{}]", accountDto);

        Account managedAccount = accountDao.insert(
                Optional.ofNullable(accountMapper.toAccount(accountDto))
                        .orElseThrow(() -> new AccountException("Invalid account data")));
        AccountResDto account = accountMapper.toAccountDto(managedAccount);

        log.info("createAccount from: account=[{}]", account);
        return account;
    }

    @Override
    @LogDataSourceError
    public void deleteAccount(@NonNull Long id) {
        log.info("deleteAccount to: id=[{}]", id);

        accountDao.deleteById(id);

        log.info("deleteAccount from");
    }
}
