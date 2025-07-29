package com.pd.expense_tracker.repository;

import com.pd.expense_tracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCategory_Name(String categoryName);

    @Query(value = "SELECT SUM(e.amount) FROM expenses e WHERE id = ?1",nativeQuery = true)
    double sumExpensesByUser(Long id);
}
