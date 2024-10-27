package pt.pmrelvas.pmr_expense_tracker.repositories;

import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll(CategoryFilters filters);

    Category upsert(Category category);

    void delete(long id);
}
