package pt.pmrelvas.pmr_expense_tracker.entities.filters;

public record SortFilter<E>(
        E field,
        SortDirection direction
) {
}
