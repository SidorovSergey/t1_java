package ru.t1.java.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.t1.java.demo.dto.AccountResDto;
import ru.t1.java.demo.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Import(AccountControllerTestTest.ConfigurationTest.class)
@ContextConfiguration(classes = AccountController.class)
public class AccountControllerTestTest extends BaseControllerTest {

    @MockBean
    AccountService accountServer;

    @TestConfiguration
    static class ConfigurationTest {

    }

    @Override
    protected Object getStandalone() {
        return new AccountController(accountServer);
    }

    @Test
    void getAccountTest() throws Exception {
        // setup
        when(accountServer.getAccount(any()))
                .thenReturn((AccountResDto) new AccountResDto()
                        .setId(191L)
                        .setAccountType("DEBIT")
                        .setClientId(471L)
                        .setBalance(new BigDecimal("2959611467.04")));
        String expectedResponse = "{\"balance\":2959611467.04,\"id\":191,\"client_id\":471,\"account_type\":\"DEBIT\"}";
        RequestBuilder request = MockMvcRequestBuilders.get("/account/")
                .param("id", "191")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // when
        send(request, expectedResponse);
    }

    @Test
    void getAccountsTest() throws Exception {
        // setup
        when(accountServer.getAccounts(any()))
                .thenReturn(List.of(
                        (AccountResDto) new AccountResDto()
                                .setId(191L)
                                .setAccountType("DEBIT")
                                .setClientId(471L)
                                .setBalance(new BigDecimal("2959611467.04")),
                        (AccountResDto) new AccountResDto()
                                .setId(509L)
                                .setAccountType("CREDIT")
                                .setClientId(471L)
                                .setBalance(new BigDecimal("7110959058.99"))));
        String expectedResponse = "[{\"balance\":2959611467.04,\"id\":191,\"client_id\":471,\"account_type\":\"DEBIT\"},{\"balance\":7110959058.99,\"id\":509,\"client_id\":471,\"account_type\":\"CREDIT\"}]";
        RequestBuilder request = MockMvcRequestBuilders.get("/account/accounts")
                .param("client_id", "471")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // when
        send(request, expectedResponse);
    }

    @Test
    void createAccountTest() throws Exception {
        // setup
        when(accountServer.createAccount(any()))
                .thenReturn((AccountResDto) new AccountResDto()
                        .setId(3011L)
                        .setAccountType("CREDIT")
                        .setClientId(471L)
                        .setBalance(new BigDecimal("0.22")));
        String expectedResponse = "{\"balance\":0.22,\"id\":3011,\"client_id\":471,\"account_type\":\"CREDIT\"}";
        RequestBuilder request = MockMvcRequestBuilders.post("/account/create")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content("{\"balance\":0.22,\"client_id\":471,\"account_type\":\"CREDIT\"}");

        // when
        send(request, expectedResponse);
    }

    @Test
    void deleteAccountTest() throws Exception {
        // setup
        RequestBuilder request = MockMvcRequestBuilders.delete("/account/delete")
                .param("id", "3004")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // when
        send(request);
    }
}
