package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.facade.interfaces.BudgetFacade;
import com.pd.expense_tracker.model.Budget;
import com.pd.expense_tracker.repository.BudgetRepository;
import com.pd.expense_tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetFacadeImpl implements BudgetFacade {

    private final ExpenseRepository expenseRepository;

    private final BudgetRepository budgetRepository;

    @Autowired
    public BudgetFacadeImpl(ExpenseRepository expenseRepository, BudgetRepository budgetRepository) {
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public double getUserBudget(Long userId) {
        return budgetRepository.findByUser_UserIdentifier(userId)
                .map(Budget::getMaxLimit)
                .orElse(0.0);
    }

    @Override
    public double getTotalSpent(Long id) {
        return expenseRepository.sumExpensesByUser(id);
    }
}

