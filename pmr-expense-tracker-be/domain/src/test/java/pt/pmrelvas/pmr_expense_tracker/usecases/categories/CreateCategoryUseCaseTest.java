package pt.pmrelvas.pmr_expense_tracker.usecases.categories;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.fake.FakeCategories;
import pt.pmrelvas.pmr_expense_tracker.repositories.CategoryRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @Captor
    ArgumentCaptor<Category> categoryCaptor;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    void executeSuccess() {
        // prepare
        when(categoryRepository.upsert(any())).thenReturn(FakeCategories.CAR);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // execute
        Category result = createCategoryUseCase.execute(FakeCategories.CAR);

        // verify
        verify(categoryRepository).upsert(categoryCaptor.capture());
        Category categoryToCreate = categoryCaptor.getValue();
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(result).isEqualTo(FakeCategories.CAR);
            soft.assertThat(categoryToCreate.createdAt()).isAfter(now);
            soft.assertThat(categoryToCreate.updatedAt()).isAfter(now);
            soft.assertThat(categoryToCreate)
                    .usingRecursiveComparison()
                    .ignoringFields("createdAt", "updatedAt")
                    .isEqualTo(FakeCategories.CAR);
        });
    }
}
