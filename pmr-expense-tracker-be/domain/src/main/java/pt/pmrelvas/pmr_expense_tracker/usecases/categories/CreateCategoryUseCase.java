package pt.pmrelvas.pmr_expense_tracker.usecases.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public Category execute(Category category) {
        return categoryRepository.upsert(category.toBuilder()
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build());
    }
}
