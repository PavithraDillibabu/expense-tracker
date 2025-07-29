package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.DTO.CategoryWithExpenseCountDTO;
import com.pd.expense_tracker.facade.interfaces.CategoryFacade;
import com.pd.expense_tracker.model.Category;
import com.pd.expense_tracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryFacadeImpl implements CategoryFacade {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryFacadeImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll().stream()
                .filter(c -> !c.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> getById(Long id) {
        return categoryRepository.findById(id)
                .filter(c -> !c.isDeleted());
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void softDeleteCategory(Long id) {
        categoryRepository.findById(id).ifPresent(c -> {
            c.setDeleted(true);
            categoryRepository.save(c);
        });
    }

    @Override
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<CategoryWithExpenseCountDTO> getCategoriesWithExpenseCount() {
        return categoryRepository.findAll().stream()
                .filter(c -> !c.isDeleted())
                .map(c -> new CategoryWithExpenseCountDTO(
                        c.getId(),
                        c.getName(),
                        c.getExpenses() != null ? c.getExpenses().size() : 0L
                )).collect(Collectors.toList());
    }

    @Override
    public List<Category> bulkCreateCategories(List<Category> categories) {
        List<Category> categoryList = categories.stream()
                .map(category -> {
                    Category c = new Category();
                    c.setName(category.getName());
                    return c;
                }).toList();

        return categoryRepository.saveAll(categories);
    }

    @Override
    public List<Category> getMostUsedCategories(int topN) {
        return categoryRepository.findTopCategories((Pageable) PageRequest.of(0, topN));
    }
}