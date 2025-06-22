package pt.pmrelvas.pmr_expense_tracker.usecases.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.pmrelvas.pmr_expense_tracker.repositories.ExpenseRepository;

@Component
@RequiredArgsConstructor
public class DeleteExpenseUseCase {

    private final ReadExpenseUseCase readExpenseUseCase;
    private final ExpenseRepository expenseRepository;

    public void execute(long id) {
        readExpenseUseCase.executeFindByIdOrThrow(id);
        expenseRepository.deleteById(id);
    }
} 