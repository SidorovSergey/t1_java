package ru.t1.java.demo.kafka;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.AccountResDto;
import ru.t1.java.demo.service.AccountService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountConsumer {

    private final AccountService accountService;

    @KafkaListener(id = "${t1-demo.kafka.consumer.account.group-id}",
            topics = "${t1-demo.kafka.consumer.account.topic}",
            containerFactory = "accountContainerFactory")
    public void listen(Acknowledgment ack,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key,
                       @Payload List<AccountDto> accounts) {
        log.debug("Messages received: key=[{}], accounts=[{}]", key, accounts);

        try {
            accounts.stream()
                    .filter(Objects::nonNull)
                    .forEach(account -> handler(key, account));
        } catch (Exception ex) {
            log.error("Processing failed for account consumer: key=[{}], accounts=[{}]", key, accounts, ex);
        } finally {
            ack.acknowledge();
        }
    }

    private void handler(@NonNull String key, @NonNull AccountDto account) {
        try {
            AccountResDto managedAccount = accountService.createAccount(account);
            log.info("Created account: key=[{}], account=[{}]", key, managedAccount);
        } catch (Exception ex) {
            log.error("Fail create account: key=[{}], account=[{}]", key, account, ex);
        }
    }

}
