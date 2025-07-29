package com.pd.expense_tracker.exception;

public class ExpenseConversionException extends RuntimeException {
    public ExpenseConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
