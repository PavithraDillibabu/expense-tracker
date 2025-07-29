package com.pd.expense_tracker.facade.interfaces;

import com.pd.expense_tracker.DTO.ExpenseDTO;
import com.pd.expense_tracker.model.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseFacade {

    void create(Expense expense);
    List<Expense> getAll();
    Optional<Expense> getById(Long id);
    void update(Long id, Expense expense);
    void delete(Long id);
    List<ExpenseDTO> getExpensesByCategory(String name);
}
