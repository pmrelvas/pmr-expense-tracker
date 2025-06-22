package pt.pmrelvas.pmr_expense_tracker.usecases.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pt.pmrelvas.pmr_expense_tracker.entities.Expense;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.ExpenseFilters;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.PageFilter;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.SortFilter;
import pt.pmrelvas.pmr_expense_tracker.exceptions.NotFoundException;
import pt.pmrelvas.pmr_expense_tracker.repositories.ExpenseRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadExpenseUseCase {

    private final ExpenseRepository expenseRepository;

    public List<Expense> executeFindAll(ExpenseFilters filters, SortFilter<Expense.SortingField> sortFilter, PageFilter pageFilter) {
        return expenseRepository.findAll(filters, sortFilter, pageFilter);
    }

    public Expense executeFindByIdOrThrow(long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> NotFoundException.buildForExpense(id));
    }
} 