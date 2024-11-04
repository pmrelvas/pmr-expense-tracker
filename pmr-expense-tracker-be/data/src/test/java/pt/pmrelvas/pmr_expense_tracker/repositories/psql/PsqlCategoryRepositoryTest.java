package pt.pmrelvas.pmr_expense_tracker.repositories.psql;

import org.junit.jupiter.api.Test;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.fake.FakeCategories;

import static org.assertj.core.api.Assertions.assertThat;

public class PsqlCategoryRepositoryTest extends BaseDatabaseTest {

    @Test
    void upsertCategoryShouldSucceed() {
        Category category = categoryRepository.upsert(FakeCategories.CAR);

        assertThat(category).isEqualTo(FakeCategories.CAR);
    }

    @Test
    void upsertCategoryWithDuplicateCodeShouldFail() {

    }

    @Test
    void deleteCategoryShouldSucceed() {

    }

    @Test
    void findAllCategoriesWithoutFilteringShouldSucceed() {

    }

    @Test
    void findAllCategoriesFilteringByCodeShouldSucceed() {

    }

    @Test
    void findAllCategoriesFilteringByCodeWithPartialMatchShouldSucceed() {

    }

    @Test
    void findAllCategoriesFilteringByNameShouldSucceed() {

    }

    @Test
    void findAllCategoriesFilteringByNameWithPartialMatchShouldSucceed() {

    }

    @Test
    void findAllCategoriesFilteringByParentCategoryShouldSucceed() {

    }
}
