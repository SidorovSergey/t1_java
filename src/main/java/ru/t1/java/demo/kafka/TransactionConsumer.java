package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;
import java.util.Objects;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionConsumer implements KafkaConsumer<TransactionDto> {

    private final TransactionService transactionService;

    @Override
    @KafkaListener(id = "${t1-demo.kafka.consumers.transaction.group-id}",
            topics = "${t1-demo.kafka.consumers.transaction.topic}",
            containerFactory = "transactionContainerFactory")
    public void listen(Acknowledgment ack,
                       @Header(RECEIVED_KEY) String key,
                       @Payload List<TransactionDto> transactions) {
        log.debug("Messages received: key=[{}], transactions=[{}]", key, transactions);

        try {
            transactions.stream()
                    .filter(Objects::nonNull)
                    .forEach(transaction -> transactionService.handleTransaction(key, transaction));
        } catch (Exception ex) {
            log.error("Processing failed for transaction consumer: key=[{}], transactions=[{}]", key, transactions, ex);
        } finally {
            ack.acknowledge();
        }
    }
}
