package com.pd.expense_tracker.model;

public enum ExpenseType {
    DEBIT,
    CREDIT;
    public String getValueAsString() {
        return name().toLowerCase();  // or customize format here
    }
}
