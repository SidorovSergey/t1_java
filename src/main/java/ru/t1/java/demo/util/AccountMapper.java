package ru.t1.java.demo.util;

import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.AccountResDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.AccountType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public abstract class AccountMapper {

    @Nullable
    public AccountResDto toAccountResDto(@Nullable Account account) {
        if (isNull(account)) {
            return null;
        }
        return (AccountResDto) new AccountResDto()
                .setId(account.getId())
                .setAccountId(account.getAccountId())
                .setClientId(account.getClientId())
                .setAccountType(account.getAccountType().name())
                .setAccountStatus(account.getAccountStatus().name())
                .setBalance(account.getBalance());
    }

    @Nullable
    public List<AccountResDto> toAccountDtos(@Nullable List<Account> accounts) {
        if (isNull(accounts)) {
            return null;
        }

        List<AccountResDto> dtos = new ArrayList<>();

        accounts.forEach(account ->
                Optional.ofNullable(toAccountResDto(account))
                        .ifPresent(dtos::add));

        return dtos;
    }

    @Nullable
    public Account toAccount(@Nullable AccountDto accountDto) {
        if (isNull(accountDto)) {
            return null;
        }
        return new Account()
                .setClientId(accountDto.getClientId())
                .setAccountId(accountDto.getAccountId())
                .setAccountType(AccountType.valueOf(accountDto.getAccountType()))
                .setAccountStatus(AccountStatus.valueOf(accountDto.getAccountStatus()))
                .setBalance(accountDto.getBalance());
    }

}
