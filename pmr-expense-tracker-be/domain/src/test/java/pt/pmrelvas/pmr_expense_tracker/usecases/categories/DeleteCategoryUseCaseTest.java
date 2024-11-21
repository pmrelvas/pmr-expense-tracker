package pt.pmrelvas.pmr_expense_tracker.usecases.categories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @Mock
    private ReadCategoryUseCase readCategoryUseCase;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Test
    void executeSuccess() {
        // prepare
        long id = 123L;

        // execute
        deleteCategoryUseCase.execute(id);

        // verify
        verify(readCategoryUseCase).executeFindByIdOrThrow(id);
        verify(categoryRepository).deleteById(id);
    }
}
