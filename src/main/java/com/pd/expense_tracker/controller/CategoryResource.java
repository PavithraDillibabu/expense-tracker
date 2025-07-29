package com.pd.expense_tracker.controller;

import com.pd.expense_tracker.DTO.CategoryWithExpenseCountDTO;
import com.pd.expense_tracker.DTO.ResponseDTO;
import com.pd.expense_tracker.facade.interfaces.CategoryFacade;
import com.pd.expense_tracker.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category", description = "Category management APIs")
public class CategoryResource {

    @Autowired
    private CategoryFacade categoryFacade;

    @Operation(summary = "Create new category")
    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryFacade.create(category);
    }

    @Operation(summary = "Get all category")
    @GetMapping
    public List<Category> getAll() {
        return categoryFacade.getAll();
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return categoryFacade.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete category permanently")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        categoryFacade.delete(id);
        return ResponseEntity.ok(new ResponseDTO("Deleted"));
    }

    @Operation(summary = "Soft delete category")
    @DeleteMapping("/{id}/soft")
    public ResponseEntity<ResponseDTO> softDelete(@PathVariable Long id) {
        categoryFacade.softDeleteCategory(id);
        return ResponseEntity.ok(new ResponseDTO("Soft Delete Success"));
    }

    @Operation(summary = "Search categories by keyword")
    @GetMapping("/search")
    public List<Category> search(@RequestParam String keyword) {
        return categoryFacade.searchCategories(keyword);
    }

    @Operation(summary = "Get categories with expense count")
    @GetMapping("/with-expense-count")
    public List<CategoryWithExpenseCountDTO> withExpenseCount() {
        return categoryFacade.getCategoriesWithExpenseCount();
    }

    @Operation(summary = "Bulk create categories")
    @PostMapping("/bulk")
    public List<Category> bulkCreate(@RequestBody List<Category> categories) {
        return categoryFacade.bulkCreateCategories(categories);
    }

    @Operation(summary = "Get most used categories")
    @GetMapping("/most-used")
    public List<Category> mostUsed(@RequestParam(defaultValue = "5") int topN) {
        return categoryFacade.getMostUsedCategories(topN);
    }


}
