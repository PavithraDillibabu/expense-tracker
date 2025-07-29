package com.pd.expense_tracker.controller;

import com.pd.expense_tracker.DTO.PaymentDTO;
import com.pd.expense_tracker.facade.SplitExpenseFacadeImpl;
import com.pd.expense_tracker.model.ExpenseSplit;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/split")
public class SplitExpenseResource {

    private final SplitExpenseFacadeImpl splitExpenseFacade;

    public SplitExpenseResource(SplitExpenseFacadeImpl splitExpenseFacade) {
        this.splitExpenseFacade = splitExpenseFacade;
    }

    @PostMapping
    public ExpenseSplit addExpense(@RequestParam String userId, @RequestParam double amount) {
        return splitExpenseFacade.addExpense(userId, amount);
    }

    @GetMapping
    public List<ExpenseSplit> getAllExpenses() {
        return splitExpenseFacade.getAllExpenses();
    }

    @GetMapping("/calculate")
    public List<PaymentDTO> calculate() {
        return splitExpenseFacade.calculateBalances();
    }
}
