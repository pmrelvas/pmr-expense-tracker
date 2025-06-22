package pt.pmrelvas.pmr_expense_tracker.payloads.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Expense request payload")
@Builder(toBuilder = true)
public record ExpenseApiRequestPayload(
        @Schema(description = "Expense amount")
        @NotNull
        BigDecimal amount,
        @Schema(description = "Total balance")
        BigDecimal totalBalance,
        @Schema(description = "Expense description")
        @NotBlank
        String description,
        @Schema(description = "Operation date")
        @NotNull
        LocalDateTime operationDate,
        @Schema(description = "Transaction date")
        @NotNull
        LocalDateTime transactionDate,
        @Schema(description = "Expense source")
        String source,
        @Schema(description = "Category identifier")
        Long categoryId
) {

    public Expense toEntity() {
        return Expense.builder()
                .amount(amount)
                .totalBalance(totalBalance)
                .description(description)
                .operationDate(operationDate)
                .transactionDate(transactionDate)
                .source(source)
                .category(categoryId == null ? null : Category.builder()
                        .id(categoryId)
                        .build())
                .build();
    }
} 