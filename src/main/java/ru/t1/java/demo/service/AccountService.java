package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.AccountResDto;

import java.util.List;

public interface AccountService {

    AccountResDto getAccount(Long id);

    List<AccountResDto> getAccounts(Long clientId);

    AccountResDto createAccount(AccountDto accountDto);

    void deleteAccount(Long id);

}
