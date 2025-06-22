package pt.pmrelvas.pmr_expense_tracker.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense;
import pt.pmrelvas.pmr_expense_tracker.entities.fake.FakeExpenses;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.ExpenseFilters;
import pt.pmrelvas.pmr_expense_tracker.exceptions.ErrorCode;
import pt.pmrelvas.pmr_expense_tracker.exceptions.NotFoundException;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.ExpenseApiRequestPayload;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.ExpenseApiResponsePayload;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.ErrorApiResponsePayload;
import pt.pmrelvas.pmr_expense_tracker.usecases.expenses.CreateExpenseUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.expenses.DeleteExpenseUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.expenses.ReadExpenseUseCase;
import pt.pmrelvas.pmr_expense_tracker.usecases.expenses.UpdateExpenseUseCase;
import utils.ResourceLoader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@WebMvcTest(ExpenseController.class)
public class ExpenseControllerApiTest extends BaseApiTest {

    private static final String BASE_URL = "/api/v1/expenses";

    @MockBean
    private ReadExpenseUseCase readExpenseUseCase;
    @MockBean
    private UpdateExpenseUseCase updateExpenseUseCase;
    @MockBean
    private CreateExpenseUseCase createExpenseUseCase;
    @MockBean
    private DeleteExpenseUseCase deleteExpenseUseCase;

    @Test
    void fetchAllWithFiltersShouldSuccessfullyCallUseCaseWithEmptyFilters() throws Exception {
        // Arrange
        when(readExpenseUseCase.executeFindAll(ExpenseFilters.builder().build(), null, null))
                .thenReturn(FakeExpenses.ALL);

        // Act
        List<ExpenseApiResponsePayload> result = requestList(RequestDto.<ExpenseApiResponsePayload>builder()
                .requestBuilder(get(BASE_URL))
                .resultMatcher(status().isOk())
                .clazz(ExpenseApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .containsExactlyInAnyOrderElementsOf(FakeExpenses.ALL.stream()
                        .map(ExpenseApiResponsePayload::new)
                        .toList());
    }

    @Test
    void fetchAllWithFiltersShouldSuccessfullyCallUseCaseWithFilters() throws Exception {
        // Arrange
        ExpenseFilters filters = ExpenseFilters.builder()
                .amount(new BigDecimal("50.25"))
                .operationDate(LocalDateTime.now())
                .transactionDate(LocalDateTime.now())
                .source("Credit Card")
                .categoryId(2L)
                .build();
        when(readExpenseUseCase.executeFindAll(filters, null, null))
                .thenReturn(FakeExpenses.ALL);

        // Act
        List<ExpenseApiResponsePayload> result = requestList(RequestDto.<ExpenseApiResponsePayload>builder()
                .requestBuilder(get(BASE_URL)
                        .param("amount", "50.25")
                        .param("operationDate", LocalDateTime.now().toString())
                        .param("transactionDate", LocalDateTime.now().toString())
                        .param("source", "Credit Card")
                        .param("categoryId", "2"))
                .resultMatcher(status().isOk())
                .clazz(ExpenseApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .containsExactlyInAnyOrderElementsOf(FakeExpenses.ALL.stream()
                        .map(ExpenseApiResponsePayload::new)
                        .toList());
    }

    @Test
    void fetchByIdShouldRetrieveExpenseWithId() throws Exception {
        // Arrange
        final long id = 123L;
        when(readExpenseUseCase.executeFindByIdOrThrow(id))
                .thenReturn(FakeExpenses.GROCERY_EXPENSE);

        // Act
        ExpenseApiResponsePayload result = request(RequestDto.<ExpenseApiResponsePayload>builder()
                .requestBuilder(get("%s/%d".formatted(BASE_URL, id)))
                .resultMatcher(status().isOk())
                .clazz(ExpenseApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .isEqualTo(new ExpenseApiResponsePayload(FakeExpenses.GROCERY_EXPENSE));
    }

    @Test
    void fetchByIdShouldRetrieveNotFound() throws Exception {
        // Arrange
        final long id = 123L;
        doThrow(NotFoundException.buildForExpense(id))
                .when(readExpenseUseCase).executeFindByIdOrThrow(id);

        // Act
        ErrorApiResponsePayload result = request(RequestDto.<ErrorApiResponsePayload>builder()
                .requestBuilder(get("%s/%d".formatted(BASE_URL, id)))
                .resultMatcher(status().isNotFound())
                .clazz(ErrorApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .isEqualTo(ErrorApiResponsePayload.builder()
                        .errorCode(ErrorCode.EXPENSE_NOT_FOUND.getCode())
                        .message(NotFoundException.buildForExpense(id).getMessage())
                        .fields(List.of(ErrorApiResponsePayload.Field.builder()
                                        .name("id")
                                        .value("123")
                                .build()))
                        .build());
    }

    @Test
    void createExpenseWithAllFieldsSuccess() throws Exception {
        // Arrange
        final String payload = ResourceLoader.getStringFromFile("expenses/valid_expense_request_payload_all_fields.json");
        final Expense expense = Expense.builder()
                .amount(new BigDecimal("50.25"))
                .totalBalance(new BigDecimal("1250.75"))
                .description("Grocery shopping")
                .operationDate(LocalDateTime.parse("2024-01-15T10:30:00"))
                .transactionDate(LocalDateTime.parse("2024-01-15T10:30:00"))
                .source("Credit Card")
                .category(Category.builder()
                        .id(2L)
                        .build())
                .build();
        final Expense expenseWithId = expense.toBuilder()
                .id(321L)
                .build();
        when(createExpenseUseCase.execute(expense))
                .thenReturn(expenseWithId);

        // Act
        ExpenseApiResponsePayload result = request(RequestDto.<ExpenseApiResponsePayload>builder()
                .requestBuilder(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .resultMatcher(status().isCreated())
                .clazz(ExpenseApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result).isEqualTo(new ExpenseApiResponsePayload(expenseWithId));
    }

    @Test
    void createExpenseWithOnlyRequiredFieldsSuccess() throws Exception {
        // Arrange
        final String payload = ResourceLoader.getStringFromFile("expenses/valid_expense_request_payload_optional_fields.json");
        final Expense expense = Expense.builder()
                .amount(new BigDecimal("50.25"))
                .description("Grocery shopping")
                .operationDate(LocalDateTime.parse("2024-01-15T10:30:00"))
                .transactionDate(LocalDateTime.parse("2024-01-15T10:30:00"))
                .build();
        final Expense expenseWithId = expense.toBuilder()
                .id(321L)
                .build();
        when(createExpenseUseCase.execute(expense))
                .thenReturn(expenseWithId);

        // Act
        ExpenseApiResponsePayload result = request(RequestDto.<ExpenseApiResponsePayload>builder()
                .requestBuilder(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .resultMatcher(status().isCreated())
                .clazz(ExpenseApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result).isEqualTo(new ExpenseApiResponsePayload(expenseWithId));
    }

    @Test
    void createExpenseWithInvalidPayloadShouldReturnBadRequest() throws Exception {
        // Arrange
        final String payload = ResourceLoader.getStringFromFile("expenses/invalid_expense_request_payload.json");

        // Act
        mockMvc
                .perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        verifyNoInteractions(createExpenseUseCase);
    }

    @Test
    void updateExpenseWithAllFieldsSuccess() throws Exception {
        // Arrange
        final long id = 123L;
        final String payload = ResourceLoader.getStringFromFile("expenses/valid_expense_request_payload_all_fields.json");
        final Expense expense = Expense.builder()
                .amount(new BigDecimal("50.25"))
                .totalBalance(new BigDecimal("1250.75"))
                .description("Grocery shopping")
                .operationDate(LocalDateTime.parse("2024-01-15T10:30:00"))
                .transactionDate(LocalDateTime.parse("2024-01-15T10:30:00"))
                .source("Credit Card")
                .category(Category.builder()
                        .id(2L)
                        .build())
                .build();
        final Expense updatedExpense = expense.toBuilder()
                .id(id)
                .build();
        when(updateExpenseUseCase.execute(id, expense))
                .thenReturn(updatedExpense);

        // Act
        ExpenseApiResponsePayload result = request(RequestDto.<ExpenseApiResponsePayload>builder()
                .requestBuilder(put("%s/%d".formatted(BASE_URL, id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .resultMatcher(status().isOk())
                .clazz(ExpenseApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result).isEqualTo(new ExpenseApiResponsePayload(updatedExpense));
    }

    @Test
    void updateExpenseWithInvalidPayloadShouldReturnBadRequest() throws Exception {
        // Arrange
        final long id = 123L;
        final String payload = ResourceLoader.getStringFromFile("expenses/invalid_expense_request_payload.json");

        // Act
        mockMvc
                .perform(put("%s/%d".formatted(BASE_URL, id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        verifyNoInteractions(updateExpenseUseCase);
    }

    @Test
    void deleteExpenseByIdSuccess() throws Exception {
        // Arrange
        final long id = 123L;

        // Act
        mockMvc
                .perform(delete("%s/%d".formatted(BASE_URL, id)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        verify(deleteExpenseUseCase).execute(id);
    }

    @Test
    void deleteExpenseByIdNotFound() throws Exception {
        // Arrange
        final long id = 123L;
        doThrow(NotFoundException.buildForExpense(id))
                .when(deleteExpenseUseCase).execute(id);

        // Act
        ErrorApiResponsePayload result = request(RequestDto.<ErrorApiResponsePayload>builder()
                .requestBuilder(delete("%s/%d".formatted(BASE_URL, id)))
                .resultMatcher(status().isNotFound())
                .clazz(ErrorApiResponsePayload.class)
                .build());

        // Assert
        assertThat(result)
                .isEqualTo(ErrorApiResponsePayload.builder()
                        .errorCode(ErrorCode.EXPENSE_NOT_FOUND.getCode())
                        .message(NotFoundException.buildForExpense(id).getMessage())
                        .fields(List.of(ErrorApiResponsePayload.Field.builder()
                                .name("id")
                                .value("123")
                                .build()))
                        .build());
    }
} 