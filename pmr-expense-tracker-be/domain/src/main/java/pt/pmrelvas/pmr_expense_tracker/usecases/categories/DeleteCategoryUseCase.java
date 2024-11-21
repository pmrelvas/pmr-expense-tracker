package pt.pmrelvas.pmr_expense_tracker.usecases.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

@Component
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final ReadCategoryUseCase readCategoryUseCase;
    private final CategoryRepository categoryRepository;

    public void execute(long id) {
        readCategoryUseCase.executeFindByIdOrThrow(id);
        categoryRepository.deleteById(id);
    }
}
