package com.pd.expense_tracker.DTO;

public record ExpenseEventDTO(Long userId, Double amount, String category) {
}