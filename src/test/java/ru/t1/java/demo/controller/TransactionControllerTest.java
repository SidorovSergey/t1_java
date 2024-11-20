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
import ru.t1.java.demo.dto.TransactionResDto;
import ru.t1.java.demo.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Import(TransactionControllerTest.ConfigurationTest.class)
@ContextConfiguration(classes = TransactionController.class)
public class TransactionControllerTest extends BaseControllerTest {

    @MockBean
    TransactionService transactionService;

    @TestConfiguration
    static class ConfigurationTest {

    }

    @Override
    protected Object getStandalone() {
        return new TransactionController(transactionService);
    }

    @Test
    void getTransactionTest() throws Exception {
        // setup
        when(transactionService.getTransaction(any()))
                .thenReturn((TransactionResDto) new TransactionResDto()
                        .setId(200L)
                        .setAccountId(191L)
                        .setAmount(new BigDecimal("2959611467.04"))
                        .setCreateTime(LocalDateTime.of(2024, 9, 1, 23, 51, 20)));
        String expectedResponse = "{\"amount\":2959611467.04,\"id\":200,\"account_id\":191,\"transaction_id\":null,\"transaction_status\":null,\"create_time\":[2024,9,1,23,51,20]}";
        RequestBuilder request = MockMvcRequestBuilders.get("/transaction/")
                .param("id", "200")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // when
        send(request, expectedResponse);
    }

    @Test
    void getTransactionsTest() throws Exception {
        // setup
        when(transactionService.getTransactions(any()))
                .thenReturn(List.of(
                        (TransactionResDto) new TransactionResDto()
                                .setId(20L)
                                .setAccountId(191L)
                                .setAmount(new BigDecimal("9562165243.12"))
                                .setCreateTime(LocalDateTime.of(2024, 9, 1, 23, 51, 20)),
                        (TransactionResDto) new TransactionResDto()
                                .setId(305L)
                                .setAccountId(191L)
                                .setAmount(new BigDecimal("6728750353.69"))
                                .setCreateTime(LocalDateTime.of(2024, 2, 25, 12, 55, 18))));
        String expectedResponse = "[{\"amount\":9562165243.12,\"id\":20,\"account_id\":191,\"transaction_id\":null,\"transaction_status\":null,\"create_time\":[2024,9,1,23,51,20]},{\"amount\":6728750353.69,\"id\":305,\"account_id\":191,\"transaction_id\":null,\"transaction_status\":null,\"create_time\":[2024,2,25,12,55,18]}]";
        RequestBuilder request = MockMvcRequestBuilders.get("/transaction/transactions")
                .param("account_id", "191")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // when
        send(request, expectedResponse);
    }

    @Test
    void createTransaction() throws Exception {
        // setup
        when(transactionService.createTransaction(any()))
                .thenReturn((TransactionResDto) new TransactionResDto()
                        .setId(150L)
                        .setAccountId(389L)
                        .setAmount(new BigDecimal("0.95"))
                        .setCreateTime(LocalDateTime.of(2024, 10, 16, 16, 0, 45)));
        String expectedResponse = "{\"amount\":0.95,\"id\":150,\"account_id\":389,\"transaction_id\":null,\"transaction_status\":null,\"create_time\":[2024,10,16,16,0,45]}";
        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/create")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content("{\"amount\":0.95,\"account_id\":389,\"create_time\":\"2024-10-16T16:00:45\"}");

        // when
        send(request, expectedResponse);
    }

    @Test
    void deleteAccountTest() throws Exception {
        // setup
        RequestBuilder request = MockMvcRequestBuilders.delete("/transaction/delete")
                .param("id", "1001")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // when
        send(request);
    }

}
