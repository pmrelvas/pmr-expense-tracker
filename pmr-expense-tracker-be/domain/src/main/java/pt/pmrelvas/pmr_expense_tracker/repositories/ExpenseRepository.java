package pt.pmrelvas.pmr_expense_tracker.repositories;

import pt.pmrelvas.pmr_expense_tracker.entities.Expense;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.ExpenseFilters;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.PageFilter;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.SortFilter;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {

    List<Expense> findAll(ExpenseFilters filters, SortFilter<Expense.SortingField> sortFilter, PageFilter pageFilter);

    Optional<Expense> findById(long id);

    Expense upsert(Expense expense);

    void deleteById(long id);
}
