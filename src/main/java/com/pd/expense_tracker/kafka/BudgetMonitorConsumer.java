package com.pd.expense_tracker.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pd.expense_tracker.DTO.ExpenseEventDTO;
import com.pd.expense_tracker.facade.interfaces.BudgetFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class BudgetMonitorConsumer {

    private static final Logger logger = LogManager.getLogger(BudgetMonitorConsumer.class);

    @Autowired
    private BudgetFacade budgetFacade;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "expense-events", groupId = "expense-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenExpenseEvent(String message) throws JsonProcessingException {
        logger.info("Message Received");
        try {
            ExpenseEventDTO event = objectMapper.readValue(message, ExpenseEventDTO.class);

            double totalSpent = budgetFacade.getTotalSpent(event.userId());
            double maxBudget = budgetFacade.getUserBudget(event.userId());

            logger.info("TotalSpent:" + totalSpent + " MaxBudget: " + maxBudget);

            if (totalSpent > maxBudget) {
                logger.info("Budget exceeds for userId " + event.userId());
            }
        }catch(JsonProcessingException ex) {
            logger.error(ex);
        }
    }
}
