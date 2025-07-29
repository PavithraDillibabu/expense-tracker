package com.pd.expense_tracker.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pd.expense_tracker.DTO.ExpenseCreatedEventDTO;
import com.pd.expense_tracker.DTO.ExpenseEventDTO;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;

@Component
public class ExpenseEventProducer {

    private static final Logger logger = LogManager.getLogger(ExpenseEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ExpenseEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleExpenseCreatedEvent(ExpenseCreatedEventDTO event) {
        sendExpenseEvent(event.getEvent());
    }

    public void sendExpenseEvent(ExpenseEventDTO event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);

            kafkaTemplate.send("expense-events", eventJson).whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Sent event: {}", eventJson);
                } else {
                    logger.error("Failed to send event: {}", eventJson, ex);
                }
            });

            System.out.println("Sent event: " + eventJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }
}