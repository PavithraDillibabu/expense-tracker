package com.pd.expense_tracker.DTO;


import java.math.BigDecimal;

public record ExpenseConversionDTO(
        BigDecimal amount,
        String baseCurrency,
        String convertedCurrency,
        BigDecimal convertedAmount,
        BigDecimal exchangeRate,
        LocationDTO location
) {}
