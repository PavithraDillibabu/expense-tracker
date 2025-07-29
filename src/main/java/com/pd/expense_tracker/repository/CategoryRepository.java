package com.pd.expense_tracker.repository;

import com.pd.expense_tracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

        List<Category> findByNameContainingIgnoreCase(String keyword);

        boolean existsByNameIgnoreCase(String name);

        @Query(value = "SELECT c FROM Category c LEFT JOIN c.expenses e GROUP BY c ORDER BY COUNT(e) DESC",nativeQuery = true)
        List<Category> findTopCategories(Pageable pageable);

        @Query(value = "SELECT c FROM Category c WHERE c.updatedAt < :cutoff",nativeQuery = true)
        List<Category> findInactiveCategories(@Param("cutoff") LocalDateTime cutoff);
}

