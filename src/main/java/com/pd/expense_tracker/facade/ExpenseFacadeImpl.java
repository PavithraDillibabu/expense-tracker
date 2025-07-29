package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.DTO.*;
import com.pd.expense_tracker.exception.ExpenseConversionException;
import com.pd.expense_tracker.facade.interfaces.ExpenseFacade;
import com.pd.expense_tracker.mapper.ExpenseMapperImpl;
import com.pd.expense_tracker.model.Expense;
import com.pd.expense_tracker.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ExpenseFacadeImpl implements ExpenseFacade {

    private static final Logger logger = LogManager.getLogger(ExpenseFacadeImpl.class);

    private final ExpenseRepository expenseRepository;

    private final LocationFacadeImpl locationFacade;

    private final ExchangeRateFacadeImpl exchangeRateFacade;

    private final ApplicationEventPublisher publisher;

    @Autowired
    public ExpenseFacadeImpl(ExpenseRepository expenseRepository, LocationFacadeImpl locationFacade, ExchangeRateFacadeImpl exchangeRateFacade, ApplicationEventPublisher publisher) {
        this.expenseRepository = expenseRepository;
        this.locationFacade = locationFacade;
        this.exchangeRateFacade = exchangeRateFacade;
        this.publisher = publisher;
    }

    @Transactional
    @Override
    public void create(Expense expense) {
        Expense saved = expenseRepository.save(expense);
        ExpenseEventDTO event = new ExpenseEventDTO(
                saved.getId(),
                saved.getAmount(),
                saved.getCategory().getName()
        );

        publisher.publishEvent(new ExpenseCreatedEventDTO(event));

        logger.info("Event sent");
    }

    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }
    @Override
    public Optional<Expense> getById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public List<ExpenseDTO> getExpensesByCategory(String categoryName) {
        return expenseRepository.findByCategory_Name(categoryName)
                .stream()
                .map(ExpenseMapperImpl::mapExpenseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, Expense expense) {
        Optional<Expense> existing = expenseRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Expense not found with id " + id);
        }
        expenseRepository.save(expense);
    }

    @Override
    public ExpenseConversionDTO convertExpense(Expense expense, String fromCurrency, String toCurrency, String ip) {
        try {
            LocationDTO location = locationFacade.getLocation(ip);
            BigDecimal rate = exchangeRateFacade.getConversionRate(fromCurrency, toCurrency);
            BigDecimal amount = BigDecimal.valueOf(expense.getAmount());
            BigDecimal converted = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);

            return new ExpenseConversionDTO(
                    amount,
                    fromCurrency,
                    toCurrency,
                    converted,
                    rate,
                    location
            );
        } catch (Exception ex) {
            throw new ExpenseConversionException("Failed to execute expense conversion", ex);
        }
    }

}