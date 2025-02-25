package pt.pmrelvas.pmr_expense_tracker.payloads.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import pt.pmrelvas.pmr_expense_tracker.entities.Category;

@Schema(description = "Category request payload")
@Builder(toBuilder = true)
public record CategoryApiRequestPayload(
        @Schema(description = "Unique code that identifier the category")
        @NotBlank
        String code,
        @Schema(description = "Descriptive and human readable category name")
        @NotBlank
        String name,
        @Schema(description = "Parent category identifier")
        Long parentCategoryId
) {

    public Category toEntity() {
        return Category.builder()
                .code(code)
                .name(name)
                .parentCategory(parentCategoryId == null ? null : Category.builder()
                        .id(parentCategoryId)
                        .build())
                .build();
    }
}
