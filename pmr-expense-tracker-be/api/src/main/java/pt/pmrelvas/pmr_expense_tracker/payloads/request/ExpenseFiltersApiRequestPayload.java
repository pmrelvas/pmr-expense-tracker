package pt.pmrelvas.pmr_expense_tracker.payloads.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.ExpenseFilters;

@Schema(description = "Expense filters")
@Builder(toBuilder = true)
public record ExpenseFiltersApiRequestPayload(
        @Schema(description = "Expense amount", nullable = true)
        BigDecimal amount,
        @Schema(description = "Operation date", nullable = true)
        LocalDateTime operationDate,
        @Schema(description = "Transaction date", nullable = true)
        LocalDateTime transactionDate,
        @Schema(description = "Expense source", nullable = true)
        String source,
        @Schema(description = "Category identifier", nullable = true)
        @JsonProperty("category-id")
        Long categoryId
) {

    public ExpenseFilters toEntity() {
        return ExpenseFilters.builder()
                .amount(amount)
                .operationDate(operationDate)
                .transactionDate(transactionDate)
                .source(source)
                .categoryId(categoryId)
                .build();
    }
} 