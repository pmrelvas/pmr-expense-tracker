package pt.pmrelvas.pmr_expense_tracker.payloads.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Expense payload")
@Builder(toBuilder = true)
public record ExpenseApiResponsePayload(
        @Schema(description = "Expense unique identifier", example = "1")
        Long id,
        @Schema(description = "Expense amount", example = "100.50")
        BigDecimal amount,
        @Schema(description = "Total balance", example = "1500.75")
        BigDecimal totalBalance,
        @Schema(description = "Expense description", example = "Grocery shopping")
        String description,
        @Schema(description = "Operation date")
        LocalDateTime operationDate,
        @Schema(description = "Transaction date")
        LocalDateTime transactionDate,
        @Schema(description = "Expense source", example = "Credit Card")
        String source,
        @Schema(description = "Category payload")
        CategoryApiResponsePayload category,
        @Schema(description = "Creation LocalDateTime")
        LocalDateTime createdAt,
        @Schema(description = "Last update LocalDateTime")
        LocalDateTime updatedAt
) {

    public ExpenseApiResponsePayload(Expense expense) {
        this(
                expense.id(),
                expense.amount(),
                expense.totalBalance(),
                expense.description(),
                expense.operationDate(),
                expense.transactionDate(),
                expense.source(),
                expense.category() == null ? null : new CategoryApiResponsePayload(expense.category()),
                expense.createdAt(),
                expense.updatedAt());
    }
} 