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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @Mock
    private ReadCategoryUseCase readCategoryUseCase;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Captor
    private ArgumentCaptor<Category> categoryCaptor;

    @Test
    void executeSuccess() {
        // prepare
        Category category = FakeCategories.CAR;
        long id = 123L;
        when(readCategoryUseCase.executeFindByIdOrThrow(id))
                .thenReturn(category);
        Category categoryToUpdate = category.toBuilder()
                .name("name updated")
                .code("code updated")
                .parentCategory(FakeCategories.HOUSE)
                .build();
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // execute
        Category result = updateCategoryUseCase.execute(id, categoryToUpdate);

        // verify
        verify(categoryRepository).upsert(categoryCaptor.capture());
        Category updateReq = categoryCaptor.getValue();
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(updateReq.name()).isEqualTo("name updated");
            soft.assertThat(updateReq.code()).isEqualTo("code updated");
            soft.assertThat(updateReq.parentCategory()).isEqualTo(FakeCategories.HOUSE);
            soft.assertThat(updateReq.createdAt()).isEqualTo(category.createdAt());
            soft.assertThat(updateReq.updatedAt()).isAfter(now);
        });
    }
}
