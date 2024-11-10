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
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResDto;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionService transactionService;

    @KafkaListener(id = "${t1-demo.kafka.consumer.transaction.group-id}",
            topics = "${t1-demo.kafka.consumer.transaction.topic}",
            containerFactory = "transactionContainerFactory")
    public void listen(Acknowledgment ack,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key,
                       @Payload List<TransactionDto> transactions) {
        log.debug("Messages received: key=[{}], transactions=[{}]", key, transactions);

        try {
            transactions.stream()
                    .filter(Objects::nonNull)
                    .forEach(transaction -> handler(key, transaction));
        } catch (Exception ex) {
            log.error("Processing failed for transaction consumer: key=[{}], transactions=[{}]", key, transactions, ex);
        } finally {
            ack.acknowledge();
        }
    }

    private void handler(@NonNull String key, @NonNull TransactionDto transaction) {
        try {
            TransactionResDto managedTransaction = transactionService.createTransaction(transaction);
            log.info("Created transaction: key=[{}], transaction=[{}]", key, managedTransaction);
        } catch (Exception ex) {
            log.error("Fail create transaction: key=[{}], transaction=[{}]", key, transaction, ex);
        }
    }
}
