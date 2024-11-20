package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionResultDto;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;
import java.util.Objects;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionResultConsumer implements KafkaConsumer<TransactionResultDto> {

    private final TransactionService transactionService;

    @Override
    @KafkaListener(id = "${t1-demo.kafka.consumers.transaction_result.group-id}",
            topics = "${t1-demo.kafka.consumers.transaction_result.topic}",
            containerFactory = "transactionResultContainerFactory")
    public void listen(Acknowledgment ack,
                       @Header(RECEIVED_KEY) String key,
                       @Payload List<TransactionResultDto> transactionResults) {
        log.debug("Messages received: key=[{}], transactionResults=[{}]", key, transactionResults);

        try {
            transactionResults.stream()
                    .filter(Objects::nonNull)
                    .forEach(transactionResult -> transactionService.handleTransactionResult(key, transactionResult));
        } catch (Exception ex) {
            log.error("Processing failed for transaction consumer: key=[{}], transactionResults=[{}]", key, transactionResults, ex);
        } finally {
            ack.acknowledge();
        }
    }
}
