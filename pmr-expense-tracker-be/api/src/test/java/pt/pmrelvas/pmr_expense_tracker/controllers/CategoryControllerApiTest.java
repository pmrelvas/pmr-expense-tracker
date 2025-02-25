package pt.pmrelvas.pmr_expense_tracker.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.fake.FakeCategories;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;
import pt.pmrelvas.pmr_expense_tracker.exceptions.ErrorCode;
import pt.pmrelvas.pmr_expense_tracker.exceptions.NotFoundException;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.CategoryApiResponsePayload;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.ErrorApiResponsePayload;
import pt.pmrelvas.pmr_expense_tracker.usecases.categories.CreateCategoryUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.categories.DeleteCategoryUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.categories.ReadCategoryUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.categories.UpdateCategoryUseCase;
import utils.ResourceLoader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerApiTest extends BaseApiTest {

    private static final String BASE_URL = "/api/v1/categories";

    @MockBean
    private ReadCategoryUseCase readCategoryUseCase;
    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;
    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;
    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Test
    void fetchAllWithFiltersShouldSuccessfullyCallUseCaseWithEmptyFilters() throws Exception {
        // Arrange
        when(readCategoryUseCase.executeFindAll(CategoryFilters.builder().build()))
                .thenReturn(FakeCategories.ALL);

        // Act
        List<CategoryApiResponsePayload> result = requestList(RequestDto.<CategoryApiResponsePayload>builder()
                .requestBuilder(get(BASE_URL))
                .resultMatcher(status().isOk())
                .clazz(CategoryApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .containsExactlyInAnyOrderElementsOf(FakeCategories.ALL.stream()
                        .map(CategoryApiResponsePayload::new)
                        .toList());
    }

    @Test
    void fetchAllWithFiltersShouldSuccessfullyCallUseCaseWithFilters() throws Exception {
        // Arrange
        CategoryFilters filters = CategoryFilters.builder()
                .code("code-xpto")
                .name("name-xpto")
                .parentCategoryId(123L)
                .build();
        when(readCategoryUseCase.executeFindAll(filters))
                .thenReturn(FakeCategories.ALL);

        // Act
        List<CategoryApiResponsePayload> result = requestList(RequestDto.<CategoryApiResponsePayload>builder()
                .requestBuilder(get(BASE_URL)
                        .param("code", "code-xpto")
                        .param("name", "name-xpto")
                        .param("parentCategoryId", "123"))
                .resultMatcher(status().isOk())
                .clazz(CategoryApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .containsExactlyInAnyOrderElementsOf(FakeCategories.ALL.stream()
                        .map(CategoryApiResponsePayload::new)
                        .toList());
    }

    @Test
    void fetchByIdShouldRetrieveCategoryWithId() throws Exception {
        // Arrange
        final long id = 123L;
        when(readCategoryUseCase.executeFindByIdOrThrow(id))
                .thenReturn(FakeCategories.CAR);

        // Act
        CategoryApiResponsePayload result = request(RequestDto.<CategoryApiResponsePayload>builder()
                .requestBuilder(get("%s/%d".formatted(BASE_URL, id)))
                .resultMatcher(status().isOk())
                .clazz(CategoryApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .isEqualTo(new CategoryApiResponsePayload(FakeCategories.CAR));
    }

    @Test
    void fetchByIdShouldRetrieveNotFound() throws Exception {
        // Arrange
        final long id = 123L;
        doThrow(NotFoundException.buildForCategory(id))
                .when(readCategoryUseCase).executeFindByIdOrThrow(id);

        // Act
        ErrorApiResponsePayload result = request(RequestDto.<ErrorApiResponsePayload>builder()
                .requestBuilder(get("%s/%d".formatted(BASE_URL, id)))
                .resultMatcher(status().isNotFound())
                .clazz(ErrorApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .isEqualTo(ErrorApiResponsePayload.builder()
                        .errorCode(ErrorCode.CATEGORY_NOT_FOUND.getCode())
                        .message(NotFoundException.buildForCategory(id).getMessage())
                        .fields(List.of(ErrorApiResponsePayload.Field.builder()
                                        .name("id")
                                        .value("123")
                                .build()))
                        .build());
    }

    @Test
    void createCategoryWithAllFieldsSuccess() throws Exception {
        // Arrange
        final String payload = ResourceLoader.getStringFromFile("categories/valid_category_request_payload_all_fields.json");
        final Category category = Category.builder()
                .code("CAR")
                .name("Car")
                .parentCategory(Category.builder()
                        .id(123L)
                        .build())
                .build();
        final Category categoryWithId = category.toBuilder()
                .id(321L)
                .build();
        when(createCategoryUseCase.execute(category))
                .thenReturn(categoryWithId);

        // Act
        CategoryApiResponsePayload result = request(RequestDto.<CategoryApiResponsePayload>builder()
                .requestBuilder(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .resultMatcher(status().isCreated())
                .clazz(CategoryApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result).isEqualTo(new CategoryApiResponsePayload(categoryWithId));
    }

    @Test
    void createCategoryWithOnlyRequiredFieldsSuccess() throws Exception {
        // Arrange
        final String payload = ResourceLoader.getStringFromFile("categories/valid_category_request_payload_optional_fields.json");
        final Category category = Category.builder()
                .code("CAR")
                .name("Car")
                .build();
        final Category categoryWithId = category.toBuilder()
                .id(321L)
                .build();
        when(createCategoryUseCase.execute(category))
                .thenReturn(categoryWithId);

        // Act
        CategoryApiResponsePayload result = request(RequestDto.<CategoryApiResponsePayload>builder()
                .requestBuilder(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .resultMatcher(status().isCreated())
                .clazz(CategoryApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result).isEqualTo(new CategoryApiResponsePayload(categoryWithId));
    }

    @Test
    void createCategoryWithInvalidPayloadShouldReturnBadRequest() throws Exception {
        // Arrange
        final String payload = ResourceLoader.getStringFromFile("categories/invalid_category_request_payload.json");

        // Act
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        verifyNoInteractions(createCategoryUseCase);
    }

    @Test
    void updateCategoryWithAllFieldsSuccess() throws Exception {
        // Arrange
        final long id = 123L;
        final String payload = ResourceLoader.getStringFromFile("categories/valid_category_request_payload_all_fields.json");
        final Category category = Category.builder()
                .code("CAR")
                .name("Car")
                .parentCategory(Category.builder()
                        .id(123L)
                        .build())
                .build();
        final Category updatedCategory = category.toBuilder()
                .id(id)
                .build();
        when(updateCategoryUseCase.execute(id, category))
                .thenReturn(updatedCategory);

        // Act
        CategoryApiResponsePayload result = request(RequestDto.<CategoryApiResponsePayload>builder()
                .requestBuilder(put("%s/%d".formatted(BASE_URL, id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .resultMatcher(status().isOk())
                .clazz(CategoryApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result).isEqualTo(new CategoryApiResponsePayload(updatedCategory));
    }

    @Test
    void updateCategoryWithInvalidPayloadShouldReturnBadRequest() throws Exception {
        // Arrange
        final long id = 123L;
        final String payload = ResourceLoader.getStringFromFile("categories/invalid_category_request_payload.json");

        // Act
        mockMvc
                .perform(put("%s/%d".formatted(BASE_URL, id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        verifyNoInteractions(updateCategoryUseCase);
    }

    @Test
    void deleteCategoryByIdSuccess() throws Exception {
        // Arrange
        final long id = 123L;

        // Act
        mockMvc
                .perform(delete("%s/%d".formatted(BASE_URL, id)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        verify(deleteCategoryUseCase).execute(id);
    }

    @Test
    void deleteCategoryByIdNotFound() throws Exception {
        // Arrange
        final long id = 123L;
        doThrow(NotFoundException.buildForCategory(id))
                .when(deleteCategoryUseCase).execute(id);

        // Act
        ErrorApiResponsePayload result = request(RequestDto.<ErrorApiResponsePayload>builder()
                .requestBuilder(delete("%s/%d".formatted(BASE_URL, id)))
                .resultMatcher(status().isNotFound())
                .clazz(ErrorApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .isEqualTo(ErrorApiResponsePayload.builder()
                        .errorCode(ErrorCode.CATEGORY_NOT_FOUND.getCode())
                        .message(NotFoundException.buildForCategory(id).getMessage())
                        .fields(List.of(ErrorApiResponsePayload.Field.builder()
                                .name("id")
                                .value("123")
                                .build()))
                        .build());
    }
}
