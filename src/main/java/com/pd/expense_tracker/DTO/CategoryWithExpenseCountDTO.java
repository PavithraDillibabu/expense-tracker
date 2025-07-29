package com.pd.expense_tracker.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryWithExpenseCountDTO extends CategoryDTO{
    private long expenseCount;

    public CategoryWithExpenseCountDTO(Long id, String name, Long expenseCount){
        super(id,name);
        this.expenseCount = expenseCount;

    }

}
