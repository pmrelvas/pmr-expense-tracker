package pt.pmrelvas.pmr_expense_tracker.payloads.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;

import java.time.LocalDateTime;

@Schema(description = "Category payload")
@Builder(toBuilder = true)
public record CategoryApiResponsePayload(
        @Schema(description = "Category unique identifier", example = "1")
        Long id,
        @Schema(description = "Unique code that identifier the category", example = "CAR")
        String code,
        @Schema(description = "Descriptive and human readable category name", example = "Car")
        String name,
        @Schema(description = "Parent category payload")
        CategoryApiResponsePayload parentCategory,
        @Schema(description = "Creation LocalDateTime")
        LocalDateTime createdAt,
        @Schema(description = "Last update LocalDateTime")
        LocalDateTime updatedAt
) {

    public CategoryApiResponsePayload(Category category) {
        this(
                category.id(),
                category.code(),
                category.name(),
                category.parentCategory() == null ? null : new CategoryApiResponsePayload(category.parentCategory()),
                category.createdAt(),
                category.updatedAt());
    }
}
