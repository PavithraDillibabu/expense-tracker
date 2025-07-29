package com.pd.expense_tracker.DTO;

public record LocationDTO(
        String ip,
        String city,
        String country,
        double latitude,
        double longitude
) {}