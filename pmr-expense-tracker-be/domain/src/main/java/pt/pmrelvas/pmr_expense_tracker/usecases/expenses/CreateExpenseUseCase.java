package pt.pmrelvas.pmr_expense_tracker.usecases.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense;
import pt.pmrelvas.pmr_expense_tracker.repositories.ExpenseRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class CreateExpenseUseCase {

    private final ExpenseRepository expenseRepository;

    public Expense execute(Expense expense) {
        return expenseRepository.upsert(expense.toBuilder()
                .createdAt(LocalDateTime.now(ZoneOffset.UTC))
                .updatedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build());
    }
} 