package pt.pmrelvas.pmr_expense_tracker.repositories;

import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> findAll(CategoryFilters filters);

    Optional<Category> findById(long id);

    Category upsert(Category category);

    void deleteById(long id);
}
