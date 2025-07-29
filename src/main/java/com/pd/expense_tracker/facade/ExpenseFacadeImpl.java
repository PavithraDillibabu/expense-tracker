package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.DTO.ExpenseCreatedEventDTO;
import com.pd.expense_tracker.DTO.ExpenseDTO;
import com.pd.expense_tracker.DTO.ExpenseEventDTO;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ExpenseFacadeImpl implements ExpenseFacade {

    private static final Logger logger = LogManager.getLogger(ExpenseFacadeImpl.class);

    private final ExpenseRepository expenseRepository;

    private final ApplicationEventPublisher publisher;

    @Autowired
    public ExpenseFacadeImpl(ExpenseRepository expenseRepository, ApplicationEventPublisher publisher) {
        this.expenseRepository = expenseRepository;
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


}