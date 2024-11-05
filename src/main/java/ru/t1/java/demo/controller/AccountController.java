package ru.t1.java.demo.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.AccountResDto;
import ru.t1.java.demo.service.AccountService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController extends BaseController {

    private final AccountService accountServer;

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResDto> getAccount(@RequestParam(name = "id") Long id) {
        AccountResDto account = accountServer.getAccount(id);
        return getResponse(account);
    }

    @GetMapping(value = "/accounts", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountResDto>> getAccounts(@RequestParam(name = "client_id") Long clientId) {
        List<AccountResDto> accounts = accountServer.getAccounts(clientId);
        return getResponse(accounts);
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResDto> createAccount(@RequestBody AccountDto accountDto) {
        AccountResDto account = accountServer.createAccount(accountDto);
        return getResponse(account);
    }

    @DeleteMapping(value = "/delete", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAccount(@RequestParam(name = "id") Long id) {
        accountServer.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}
