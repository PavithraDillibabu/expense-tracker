package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.DTO.ForecastDTO;
import com.pd.expense_tracker.facade.interfaces.ExpenseForecastFacade;
import com.pd.expense_tracker.model.Expense;
import com.pd.expense_tracker.repository.ExpenseRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

import static java.util.Locale.ENGLISH;

@Service
public class ExpenseForecastFacadeImpl implements ExpenseForecastFacade {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public ForecastDTO predictNextMonthExpense(int monthsBack) {
        List<Expense> allExpenses = expenseRepository.findAll();
        LocalDate cutoff = LocalDate.now().minusMonths(monthsBack);

        // Filter expenses after cutoff
        List<Expense> filteredExpenses = allExpenses.stream()
                .filter(e -> e.getCreatedAt() != null && !e.getCreatedAt().toLocalDate().isBefore(cutoff))
                .sorted(Comparator.comparing(Expense::getCreatedAt))
                .toList();

        if (filteredExpenses.size() < 2) {
            return new ForecastDTO(0.0, "Not enough data");
        }

        SimpleRegression regression = new SimpleRegression();

// Add one data point per expense (x = index, y = amount)
        for (int i = 0; i < filteredExpenses.size(); i++) {
            regression.addData(i, filteredExpenses.get(i).getAmount());
        }

// Predict the next point (next expense, based on trend)
        double prediction = regression.predict(filteredExpenses.size());

// Format the label to show the next month name
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        String monthLabel = nextMonth.getMonth().getDisplayName(TextStyle.FULL, ENGLISH) + " " + nextMonth.getYear();

        return new ForecastDTO(prediction, monthLabel);
    }
    }
