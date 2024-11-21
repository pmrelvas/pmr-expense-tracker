package pt.pmrelvas.pmr_expense_tracker.usecases.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class UpdateCategoryUseCase {

    private final ReadCategoryUseCase readCategoryUseCase;
    private final CategoryRepository categoryRepository;

    public Category execute(long id, Category category) {
        Category existingCategory = readCategoryUseCase.executeFindByIdOrThrow(id);
        return categoryRepository.upsert(existingCategory.toBuilder()
                .name(category.name())
                .code(category.code())
                .parentCategory(category.parentCategory())
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build());
    }
}
