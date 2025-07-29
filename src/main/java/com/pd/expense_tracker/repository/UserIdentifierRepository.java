package com.pd.expense_tracker.repository;

import com.pd.expense_tracker.model.UserIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentifierRepository extends JpaRepository<UserIdentifier, String> {
}