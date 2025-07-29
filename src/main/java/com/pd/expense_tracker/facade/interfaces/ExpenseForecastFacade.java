package com.pd.expense_tracker.facade.interfaces;

import com.pd.expense_tracker.DTO.ForecastDTO;

public interface ExpenseForecastFacade {
    ForecastDTO predictNextMonthExpense(int monthsBack);
}
