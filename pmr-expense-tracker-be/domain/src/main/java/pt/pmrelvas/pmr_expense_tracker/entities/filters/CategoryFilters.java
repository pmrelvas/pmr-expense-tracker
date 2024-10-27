package pt.pmrelvas.pmr_expense_tracker.entities.filters;

import lombok.Builder;

@Builder(toBuilder = true)
public record CategoryFilters(
       String code,
       String name,
       Long parentCategoryId
) {
}
