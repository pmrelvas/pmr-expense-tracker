package pt.pmrelvas.pmr_expense_tracker.entities.filters;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder(toBuilder = true)
public record ExpenseFilters(
       BigDecimal amount,
       LocalDateTime operationDate,
       LocalDateTime transactionDate,
       String source,
       Long categoryId
) {
}
