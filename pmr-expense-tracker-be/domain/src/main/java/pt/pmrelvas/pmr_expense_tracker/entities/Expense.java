package pt.pmrelvas.pmr_expense_tracker.entities;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Expense(
        Long id,
        BigDecimal amount,
        BigDecimal totalBalance,
        String description,
        LocalDateTime operationDate,
        LocalDateTime transactionDate,
        String source,
        Category category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public enum SortingField {
        AMOUNT,
        OPERATION_DATE,
        TRANSACTION_DATE,
        SOURCE,
        CATEGORY
    }
}
