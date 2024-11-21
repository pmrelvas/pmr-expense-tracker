package pt.pmrelvas.pmr_expense_tracker.repositories.psql;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.fake.FakeCategories;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PsqlCategoryRepositoryTest extends BaseDatabaseTest {

    @Test
    void upsertAndDeleteCategoryShouldSucceed() {
        Category category = categoryRepository.upsert(FakeCategories.CAR);

        assertThat(category)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(FakeCategories.CAR);

        categoryRepository.deleteById(category.id());
    }

    @Test
    void upsertCategoryWithDuplicateCodeShouldFail() {
        Category category = categoryRepository.upsert(FakeCategories.CAR);

        assertThatThrownBy(() -> categoryRepository.upsert(FakeCategories.CAR))
                .isInstanceOf(DataIntegrityViolationException.class);

        categoryRepository.deleteById(category.id());
    }

    @Test
    void findAllCategoriesWithoutFilteringShouldSucceed() {
        FakeCategories.ALL.forEach(super::create);
        CategoryFilters filters = CategoryFilters.builder().build();

        List<Category> result = categoryRepository.findAll(filters);

        assertThat(result)
                .hasSize(FakeCategories.ALL.size())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "parentCategory.id")
                .containsExactlyInAnyOrderElementsOf(FakeCategories.ALL);
    }

    @Test
    void findAllCategoriesFilteringByCodeShouldSucceed() {
        FakeCategories.ALL.forEach(super::create);
        CategoryFilters filters = CategoryFilters.builder()
                .code(FakeCategories.CAR.code())
                .build();

        List<Category> result = categoryRepository.findAll(filters);

        assertThat(result)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "parentCategory.id")
                .containsExactly(FakeCategories.CAR);
    }

    @Test
    void findAllCategoriesFilteringByCodeWithPartialMatchShouldSucceed() {
        FakeCategories.ALL.forEach(super::create);
        CategoryFilters filters = CategoryFilters.builder()
                .code("Ue")
                .build();

        List<Category> result = categoryRepository.findAll(filters);

        assertThat(result)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "parentCategory.id")
                .containsExactly(FakeCategories.FUEL);
    }

    @Test
    void findAllCategoriesFilteringByNameShouldSucceed() {
        FakeCategories.ALL.forEach(super::create);
        CategoryFilters filters = CategoryFilters.builder()
                .name(FakeCategories.CAR.name())
                .build();

        List<Category> result = categoryRepository.findAll(filters);

        assertThat(result)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "parentCategory.id")
                .containsExactly(FakeCategories.CAR);
    }

    @Test
    void findAllCategoriesFilteringByNameWithPartialMatchShouldSucceed() {
        FakeCategories.ALL.forEach(super::create);
        CategoryFilters filters = CategoryFilters.builder()
                .name("Ue")
                .build();

        List<Category> result = categoryRepository.findAll(filters);

        assertThat(result)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "parentCategory.id")
                .containsExactly(FakeCategories.FUEL);
    }

    @Test
    void findAllCategoriesFilteringByParentCategoryShouldSucceed() {
        FakeCategories.ALL.forEach(super::create);
        Category carCategory = categoryRepository.findAll(CategoryFilters.builder().code(FakeCategories.CAR.code()).build()).stream()
                .findFirst()
                .orElse(null);
        CategoryFilters filters = CategoryFilters.builder()
                .parentCategoryId(carCategory.id())
                .build();

        List<Category> result = categoryRepository.findAll(filters);

        assertThat(result)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "parentCategory.id")
                .containsExactly(FakeCategories.FUEL);
    }

    @Test
    void findByIdWithMatchingRecordShouldSucceed() {
        Category category = create(FakeCategories.CAR);

        Optional<Category> result = categoryRepository.findById(category.id());

        assertThat(result)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(FakeCategories.CAR);
    }
}
