package pt.pmrelvas.pmr_expense_tracker.usecases.categories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.fake.FakeCategories;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;
import pt.pmrelvas.pmr_expense_tracker.exceptions.NotFoundException;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadCategoryUseCaseTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ReadCategoryUseCase readCategoryUseCase;

    @Test
    void executeFindAllSuccess() {
        // prepare
        CategoryFilters filters = CategoryFilters.builder()
                .parentCategoryId(12L)
                .code("code-xpto")
                .name("name-xpto")
                .build();
        when(categoryRepository.findAll(filters)).thenReturn(FakeCategories.ALL);

        // execute
        List<Category> result = readCategoryUseCase.executeFindAll(filters);

        // verify
        assertThat(result).isEqualTo(FakeCategories.ALL);
        verify(categoryRepository).findAll(filters);
    }

    @Test
    void executeFindByIdOrThrowSuccess() {
        // prepare
        long id = 143;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(FakeCategories.CAR));

        // execute
        Category result = readCategoryUseCase.executeFindByIdOrThrow(id);

        // verify
        assertThat(result).isEqualTo(FakeCategories.CAR);
        verify(categoryRepository).findById(id);
    }

    @Test
    void executeFindByIdOrThrowNotFoundShouldFaild() {
        // prepare
        long id = 143;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // execute
        assertThatThrownBy(() -> readCategoryUseCase.executeFindByIdOrThrow(id))
                .isInstanceOf(NotFoundException.class);

        // verify
        verify(categoryRepository).findById(id);
    }
}
