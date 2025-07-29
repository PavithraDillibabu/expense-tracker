package com.pd.expense_tracker.controller;

import com.pd.expense_tracker.DTO.ForecastDTO;
import com.pd.expense_tracker.facade.interfaces.ExpenseForecastFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsResource {

    @Autowired
    private ExpenseForecastFacade expenseForecastFacade;

    @GetMapping("/forecast")
    public ForecastDTO getForecast(@RequestParam(name = "months", required = false) Integer months) {
        int monthsBack = (months != null && months > 0) ? months : 6;
        return expenseForecastFacade.predictNextMonthExpense(monthsBack);
    }
}
