package pt.pmrelvas.pmr_expense_tracker.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import pt.pmrelvas.pmr_expense_tracker.entities.filters.CategoryFilters;

@Schema(description = "Category filters")
@Builder(toBuilder = true)
public record CategoryFiltersApiRequestPayload(
        @Schema(description = "Unique code that identifier the category", nullable = true)
        String code,
        @Schema(description = "Descriptive and human readable category name", nullable = true)
        String name,
        @Schema(description = "Parent category identifier", nullable = true)
        @JsonProperty("parent-category-id")
        Long parentCategoryId
) {

    public CategoryFilters toEntity() {
        return CategoryFilters.builder()
                .code(code)
                .name(name)
                .parentCategoryId(parentCategoryId)
                .build();
    }
}
