package pt.pmrelvas.pmr_expense_tracker.usecases.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense;
import pt.pmrelvas.pmr_expense_tracker.repositories.ExpenseRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class UpdateExpenseUseCase {

    private final ReadExpenseUseCase readExpenseUseCase;
    private final ExpenseRepository expenseRepository;

    public Expense execute(long id, Expense expense) {
        Expense existingExpense = readExpenseUseCase.executeFindByIdOrThrow(id);
        return expenseRepository.upsert(existingExpense.toBuilder()
                .amount(expense.amount())
                .totalBalance(expense.totalBalance())
                .description(expense.description())
                .operationDate(expense.operationDate())
                .transactionDate(expense.transactionDate())
                .source(expense.source())
                .category(expense.category())
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build());
    }
} 