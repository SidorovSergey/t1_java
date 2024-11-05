package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResDto;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController extends BaseController {

    private final TransactionService transactionService;

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResDto> getTransaction(@RequestParam(name = "id") Long id) {
        TransactionResDto account = transactionService.getTransaction(id);
        return getResponse(account);
    }

    @GetMapping(value = "/transactions", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionResDto>> getTransactions(@RequestParam(name = "account_id") Long accountId) {
        List<TransactionResDto> accounts = transactionService.getTransactions(accountId);
        return getResponse(accounts);
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionResDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        TransactionResDto account = transactionService.createTransaction(transactionDto);
        return getResponse(account);
    }

    @DeleteMapping(value = "/delete", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTransaction(@RequestParam(name = "id") Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
