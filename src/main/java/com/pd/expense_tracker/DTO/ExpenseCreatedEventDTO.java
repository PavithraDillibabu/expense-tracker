package com.pd.expense_tracker.DTO;

public class ExpenseCreatedEventDTO {
    private final ExpenseEventDTO event;

    public ExpenseCreatedEventDTO(ExpenseEventDTO event) {
        this.event = event;
    }

    public ExpenseEventDTO getEvent() {
        return event;
    }
}
