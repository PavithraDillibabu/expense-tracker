package com.pd.expense_tracker.controller;

import com.pd.expense_tracker.DTO.ExpenseConversionDTO;
import com.pd.expense_tracker.DTO.ExpenseDTO;
import com.pd.expense_tracker.DTO.LocationDTO;
import com.pd.expense_tracker.DTO.ResponseDTO;
import com.pd.expense_tracker.exception.ExpenseConversionException;
import com.pd.expense_tracker.facade.interfaces.ExpenseFacade;
import com.pd.expense_tracker.facade.ExchangeRateFacadeImpl;
import com.pd.expense_tracker.facade.LocationFacadeImpl;
import com.pd.expense_tracker.model.Expense;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseResource {

    private final ExpenseFacade expenseFacade;

    private final LocationFacadeImpl locationFacade;

    private final ExchangeRateFacadeImpl exchangeRateFacade;

    @Autowired
    public ExpenseResource(ExpenseFacade expenseFacade, LocationFacadeImpl locationFacade, ExchangeRateFacadeImpl exchangeRateFacade) {
        this.expenseFacade = expenseFacade;
        this.locationFacade = locationFacade;
        this.exchangeRateFacade = exchangeRateFacade;
    }

    @Operation(summary = "Get all expense")
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseFacade.getAll();
    }

    @Operation(summary = "Get expense by id")
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseFacade.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "create expense")
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createExpense(@RequestBody Expense expense) {
        expenseFacade.create(expense);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO("Expense created successfully"));
    }

    @Operation(summary = "Update expense")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        try {
            expenseFacade.update(id, expense);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO("Expense updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete Expense")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteExpense(@PathVariable Long id) {
        expenseFacade.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO("Expense deleted successfully"));
    }

    @GetMapping("/by-category-name/{categoryName}")
    @Operation(summary = "Get all expenses by category name")
    public List<ExpenseDTO> getExpenseByCategoryName(@PathVariable String categoryName) {
        return expenseFacade.getExpensesByCategory(categoryName);
    }

    @Operation(summary = "Expense Conversion")
    @GetMapping("/expenseConversion")
    public ResponseEntity<ExpenseConversionDTO> getExpenseConversion(
            @RequestBody Expense expense,
            @RequestParam String fromCurrency,
            @RequestParam(defaultValue = "INR") String toCurrency,
            @RequestParam(defaultValue = "103.21.244.1") String ip // demo IP
    ) {
        try{
        LocationDTO location = locationFacade.getLocation(ip);
        BigDecimal rate = exchangeRateFacade.getConversionRate(fromCurrency, toCurrency);
        BigDecimal amount = BigDecimal.valueOf(expense.getAmount());
        BigDecimal converted = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);

        ExpenseConversionDTO payload = new ExpenseConversionDTO(
                amount,
                fromCurrency,
                toCurrency,
                converted,
                rate,
                location
        );
        return ResponseEntity.ok(payload);
        } catch (Exception ex) {
            throw new ExpenseConversionException("Failed to execute expense", ex);
        }
    }

}
