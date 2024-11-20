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
import ru.t1.java.demo.dao.TransactionDao;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResDto;
import ru.t1.java.demo.dto.TransactionResultDto;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.service.handler.TransactionHandler;
import ru.t1.java.demo.service.impl.TransactionServiceImpl;
import ru.t1.java.demo.util.TransactionMapper;
import ru.t1.java.demo.util.TransactionMapperImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Import(TransactionServiceTest.ConfigurationTest.class)
@ContextConfiguration(classes = TransactionServiceImpl.class)
public class TransactionServiceTest {

    @MockBean
    private TransactionDao transactionDao;

    @MockBean
    private TransactionHandler transactionHandler;

    @Autowired
    private TransactionService transactionService;

    @TestConfiguration
    static class ConfigurationTest {

        @Bean
        public TransactionMapper transactionMapper() {
            return new TransactionMapperImpl();
        }
    }

    @Test
    void getTransactionTest() {
        // setup
        Long id = 5L;
        UUID transactionId = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.now();

        when(transactionDao.findById(anyLong()))
                .thenReturn(new Transaction()
                        .setId(id)
                        .setAccountId(123L)
                        .setTransactionId(transactionId)
                        .setTransactionStatus(TransactionStatus.ACCEPTED)
                        .setAmount(new BigDecimal("100.00"))
                        .setTimestamp(new Timestamp(System.currentTimeMillis()))
                        .setCreateTime(localDateTime));

        TransactionResDto expected = (TransactionResDto) new TransactionResDto()
                .setId(id)
                .setAccountId(123L)
                .setTransactionId(transactionId)
                .setTransactionStatus("ACCEPTED")
                .setAmount(new BigDecimal("100.00"))
                .setCreateTime(localDateTime);

        // when
        TransactionResDto transactionResDto = transactionService.getTransaction(id);

        // then
        assertEquals(expected, transactionResDto);
    }

    @Test
    void getTransactionsTest() {
        // setup
        Long accountId = 53L;
        UUID transactionId = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.now();

        when(transactionDao.findByAccountId(anyLong()))
                .thenReturn(List.of(
                        new Transaction()
                                .setId(44L)
                                .setAccountId(accountId)
                                .setTransactionId(transactionId)
                                .setTransactionStatus(TransactionStatus.ACCEPTED)
                                .setAmount(new BigDecimal("530.30"))
                                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                                .setCreateTime(localDateTime),
                        new Transaction()
                                .setId(345L)
                                .setAccountId(accountId)
                                .setTransactionId(transactionId)
                                .setTransactionStatus(TransactionStatus.REQUESTED)
                                .setAmount(new BigDecimal("100.00"))
                                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                                .setCreateTime(localDateTime)));

        List<TransactionResDto> expected = List.of(
                (TransactionResDto) new TransactionResDto()
                        .setId(44L)
                        .setAccountId(accountId)
                        .setTransactionId(transactionId)
                        .setTransactionStatus("ACCEPTED")
                        .setAmount(new BigDecimal("530.30"))
                        .setCreateTime(localDateTime),
                (TransactionResDto) new TransactionResDto()
                        .setId(345L)
                        .setAccountId(accountId)
                        .setTransactionId(transactionId)
                        .setTransactionStatus("REQUESTED")
                        .setAmount(new BigDecimal("100.00"))
                        .setCreateTime(localDateTime));

        // when
        List<TransactionResDto> transactionResDtos = transactionService.getTransactions(accountId);

        // then
        assertEquals(expected, transactionResDtos);
    }

    @Test
    void createTransactionTest() {
        // setup
        UUID transactionId = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.now();

        TransactionResDto expected = (TransactionResDto) new TransactionResDto()
                .setId(1L)
                .setAccountId(123L)
                .setTransactionId(transactionId)
                .setTransactionStatus("ACCEPTED")
                .setAmount(new BigDecimal("100.00"))
                .setCreateTime(localDateTime);

        when(transactionDao.insert(any()))
                .thenReturn(new Transaction()
                        .setId(1L)
                        .setAccountId(123L)
                        .setTransactionId(transactionId)
                        .setTransactionStatus(TransactionStatus.ACCEPTED)
                        .setAmount(new BigDecimal("100.00"))
                        .setCreateTime(localDateTime));

        // when
        TransactionResDto transactionResDto = transactionService.createTransaction(
                new TransactionDto()
                        .setAccountId(123L)
                        .setTransactionId(transactionId)
                        .setTransactionStatus("ACCEPTED")
                        .setAmount(new BigDecimal("100.00"))
                        .setCreateTime(localDateTime));

        // then
        assertEquals(expected, transactionResDto);
    }

    @Test
    void deleteTransactionTest() {
        // setup
        Long id = 1L;
        doNothing().when(transactionDao).deleteById(anyLong());

        // when
        transactionService.deleteTransaction(id);

        // then
        verify(transactionDao, times(1)).deleteById(id);
    }

    @Test
    void handleTransactionTest() {
        // setup
        Long accountId = 123L;
        UUID transactionId = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.now();

        TransactionDto transactionDto = new TransactionDto()
                .setAccountId(accountId)
                .setTransactionId(transactionId)
                .setTransactionStatus("ACCEPTED")
                .setAmount(new BigDecimal("100.00"))
                .setCreateTime(localDateTime);

        when(transactionHandler.request(any()))
                .thenReturn(new Transaction()
                        .setId(100L)
                        .setAccountId(accountId)
                        .setTransactionId(transactionId)
                        .setTransactionStatus(TransactionStatus.ACCEPTED)
                        .setAmount(new BigDecimal("100.00"))
                        .setCreateTime(localDateTime));

        // when
        transactionService.handleTransaction("test-key", transactionDto);

        // then
        verify(transactionHandler, times(1)).request(transactionDto);
    }

    @Test
    void handleTransactionResultAcceptTest() {
        // setup
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        TransactionResultDto transactionResultDto = new TransactionResultDto()
                .setAccountId(accountId)
                .setTransactionId(transactionId)
                .setStatus(TransactionStatus.ACCEPTED);

        when(transactionHandler.accept(any()))
                .thenReturn(new Transaction()
                        .setId(100L)
                        .setAccountId(123L)
                        .setTransactionId(transactionId)
                        .setTransactionStatus(TransactionStatus.ACCEPTED)
                        .setAmount(new BigDecimal("100.00"))
                        .setCreateTime(LocalDateTime.now()));

        // when
        transactionService.handleTransactionResult("test-key", transactionResultDto);

        // then
        verify(transactionHandler, times(1)).accept(transactionResultDto);
    }

    @Test
    void handleTransactionResultRejectTest() {
        // setup
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        TransactionResultDto transactionResultDto = new TransactionResultDto()
                .setAccountId(accountId)
                .setTransactionId(transactionId)
                .setStatus(TransactionStatus.REJECTED);

        when(transactionHandler.reject(any()))
                .thenReturn(new Transaction()
                        .setId(100L)
                        .setAccountId(123L)
                        .setTransactionId(transactionId)
                        .setTransactionStatus(TransactionStatus.REJECTED)
                        .setAmount(new BigDecimal("100.00"))
                        .setCreateTime(LocalDateTime.now()));

        // when
        transactionService.handleTransactionResult("test-key", transactionResultDto);

        // then
        verify(transactionHandler, times(1)).reject(transactionResultDto);
    }

    @Test
    void handleTransactionResultBlockTest() {
        // setup
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        TransactionResultDto transactionResultDto = new TransactionResultDto()
                .setAccountId(accountId)
                .setTransactionId(transactionId)
                .setStatus(TransactionStatus.BLOCKED);

        when(transactionHandler.block(any()))
                .thenReturn(new Transaction()
                        .setId(100L)
                        .setAccountId(123L)
                        .setTransactionId(transactionId)
                        .setTransactionStatus(TransactionStatus.BLOCKED)
                        .setAmount(new BigDecimal("100.00"))
                        .setCreateTime(LocalDateTime.now()));

        // when
        transactionService.handleTransactionResult("test-key", transactionResultDto);

        // then
        verify(transactionHandler, times(1)).block(transactionResultDto);
    }
}
