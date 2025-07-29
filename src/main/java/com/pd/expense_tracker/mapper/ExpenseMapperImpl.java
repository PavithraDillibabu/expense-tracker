package com.pd.expense_tracker.mapper;

import com.pd.expense_tracker.DTO.ExpenseDTO;
import com.pd.expense_tracker.model.Expense;
import com.pd.expense_tracker.model.ExpenseType;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapperImpl {

    public static ExpenseDTO mapExpenseDTO(Expense expense) {

        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setExpenseType(expense.getType().toString());
        dto.setDateOfTransaction(expense.getDate());
        if (expense.getCategory() != null) {
            dto.setCategoryName(expense.getCategory().getName());
            dto.setCategoryName(expense.getCategory().getName());
        }
        return dto;
    }

    public static Expense mapExpense(ExpenseDTO dto) {
        if (dto == null) return null;

        Expense expense = new Expense();
        expense.setId(dto.getId());
        expense.setAmount(dto.getAmount());
        expense.setType(ExpenseType.valueOf(dto.getExpenseType()));
        expense.setDate(dto.getDateOfTransaction());
        return expense;
    }

}
