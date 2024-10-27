package pt.pmrelvas.pmr_expense_tracker.entities;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Category(
        Long id,
        String code,
        String name,
        Category parentCategory,
        LocalDateTime createdAt
) {

}
