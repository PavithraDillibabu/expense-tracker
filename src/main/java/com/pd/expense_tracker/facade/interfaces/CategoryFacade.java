package com.pd.expense_tracker.facade.interfaces;

import com.pd.expense_tracker.DTO.CategoryWithExpenseCountDTO;
import com.pd.expense_tracker.model.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CategoryFacade {

    Category create(Category dto);

    List<Category> getAll();

    Optional<Category> getById(Long id);

    void delete(Long id);

    void softDeleteCategory(Long id);

    List<Category> searchCategories(String keyword);

    List<CategoryWithExpenseCountDTO> getCategoriesWithExpenseCount();

    List<Category> bulkCreateCategories(List<Category> categories);

    List<Category> getMostUsedCategories(int topN);

}


