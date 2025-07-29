package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.DTO.ExchangeResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExchangeRateFacadeImpl {

        private final RestTemplate restTemplate = new RestTemplate();
        private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

        public BigDecimal getConversionRate(String fromCurrency, String toCurrency) {
            ExchangeResponseDTO response = restTemplate.getForObject(API_URL + fromCurrency, ExchangeResponseDTO.class);
            Map<String, Double> rates = null;
            if (response != null && response.rates() != null) {
                rates = response.rates();
            }
            return BigDecimal.valueOf(rates.get(toCurrency));
        }
    }