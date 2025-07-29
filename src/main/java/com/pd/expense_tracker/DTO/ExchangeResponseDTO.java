package com.pd.expense_tracker.DTO;

import java.util.Map;

public record ExchangeResponseDTO(
        String base,
        String date,
        Map<String, Double> rates
) {}
