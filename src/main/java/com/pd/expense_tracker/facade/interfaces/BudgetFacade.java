package com.pd.expense_tracker.facade.interfaces;

public interface BudgetFacade {

        double getUserBudget(Long userId);
        double getTotalSpent(Long userId);
    }

