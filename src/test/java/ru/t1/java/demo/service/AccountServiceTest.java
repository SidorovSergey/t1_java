package ru.t1.java.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.t1.java.demo.dao.AccountDao;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.AccountResDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.AccountType;
import ru.t1.java.demo.service.impl.AccountServiceImpl;
import ru.t1.java.demo.util.AccountMapper;
import ru.t1.java.demo.util.AccountMapperImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Import(AccountServiceTest.ConfigurationTest.class)
@ContextConfiguration(classes = AccountServiceImpl.class)
public class AccountServiceTest {

    @MockBean
    private AccountDao accountDao;

    @Autowired
    private AccountService accountService;

    @TestConfiguration
    static class ConfigurationTest {

        @Bean
        public AccountMapper accountMapper() {
            return new AccountMapperImpl();
        }

    }

    @Test
    void getAccountTest() {
        // setup
        Long id = 1L;
        UUID accountId = UUID.randomUUID();
        AccountResDto expected = (AccountResDto) new AccountResDto()
                .setId(id)
                .setClientId(200L)
                .setAccountId(accountId)
                .setAccountType("CREDIT")
                .setAccountStatus("OPEN")
                .setBalance(new BigDecimal("100.00"));
        when(accountDao.findById(anyLong()))
                .thenReturn(new Account()
                        .setId(id)
                        .setAccountType(AccountType.CREDIT)
                        .setBalance(new BigDecimal("100.00"))
                        .setAccountId(accountId)
                        .setClientId(200L)
                        .setAccountStatus(AccountStatus.OPEN)
                        .setFrozenAmount(new BigDecimal("50.00")));

        // when
        AccountResDto accountRes = accountService.getAccount(id);

        // then
        assertEquals(expected, accountRes);
    }

    @Test
    void getAccountsTest() {
        // setup
        Long clientId = 200L;
        UUID accountIdFirst = UUID.randomUUID();
        UUID accountIdSecond = UUID.randomUUID();
        List<AccountResDto> expected = List.of(
                (AccountResDto) new AccountResDto()
                        .setId(4L)
                        .setClientId(clientId)
                        .setAccountId(accountIdFirst)
                        .setAccountType("CREDIT")
                        .setAccountStatus("OPEN")
                        .setBalance(new BigDecimal("100.00")),
                (AccountResDto) new AccountResDto()
                        .setId(43L)
                        .setClientId(clientId)
                        .setAccountId(accountIdSecond)
                        .setAccountType("DEBIT")
                        .setAccountStatus("BLOCKED")
                        .setBalance(new BigDecimal("5000.00")));

        when(accountDao.findByClientId(anyLong()))
                .thenReturn(List.of(
                        new Account()
                                .setId(4L)
                                .setClientId(clientId)
                                .setAccountId(accountIdFirst)
                                .setAccountType(AccountType.CREDIT)
                                .setAccountStatus(AccountStatus.OPEN)
                                .setBalance(new BigDecimal("100.00"))
                                .setFrozenAmount(new BigDecimal("10.00")),
                        new Account()
                                .setId(43L)
                                .setClientId(clientId)
                                .setAccountId(accountIdSecond)
                                .setAccountType(AccountType.DEBIT)
                                .setAccountStatus(AccountStatus.BLOCKED)
                                .setBalance(new BigDecimal("5000.00"))
                                .setFrozenAmount(new BigDecimal("500.00"))));

        // when
        List<AccountResDto> accountResDtos = accountService.getAccounts(clientId);

        // then
        assertEquals(expected, accountResDtos);
    }

    @Test
    void createAccountTest() {
        // setup
        UUID accountId = UUID.randomUUID();
        AccountResDto expected = (AccountResDto) new AccountResDto()
                .setId(1L)
                .setAccountId(accountId)
                .setAccountType("CREDIT")
                .setAccountStatus("OPEN")
                .setClientId(300L)
                .setBalance(new BigDecimal("100.00"));
        when(accountDao.insert(any()))
                .thenReturn(new Account()
                        .setId(1L)
                        .setAccountType(AccountType.CREDIT)
                        .setBalance(new BigDecimal("100.00"))
                        .setAccountId(accountId)
                        .setClientId(300L)
                        .setAccountStatus(AccountStatus.OPEN));

        // when
        AccountResDto accountResDto = accountService.createAccount(new AccountDto()
                .setClientId(300L)
                .setAccountId(accountId)
                .setAccountType("CREDIT")
                .setAccountStatus("OPEN")
                .setBalance(new BigDecimal("100.00")));

        // then
        assertEquals(expected, accountResDto);
    }

    @Test
    void deleteAccountTest() {
        // setup
        Long id = 1L;
        doNothing().when(accountDao).deleteById(anyLong());

        // when
        accountService.deleteAccount(id);

        // then
        verify(accountDao, times(1)).deleteById(id);
    }
}
