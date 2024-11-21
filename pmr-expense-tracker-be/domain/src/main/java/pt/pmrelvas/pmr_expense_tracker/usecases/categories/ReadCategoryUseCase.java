package pt.pmrelvas.pmr_expense_tracker.usecases.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;
import pt.pmrelvas.pmr_expense_tracker.exceptions.NotFoundException;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public List<Category> executeFindAll(CategoryFilters filters) {
        return categoryRepository.findAll(filters);
    }

    public Category executeFindByIdOrThrow(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->  NotFoundException.buildForCategory(id));
    }
}
