package com.pd.expense_tracker.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class ExpenseDTO  {
    private Long id;
    private String title;
    private Double amount;
    private Long categoryId;
    private String categoryName;
    private String expenseType;
    private LocalDate dateOfTransaction;
    private String type;

}
