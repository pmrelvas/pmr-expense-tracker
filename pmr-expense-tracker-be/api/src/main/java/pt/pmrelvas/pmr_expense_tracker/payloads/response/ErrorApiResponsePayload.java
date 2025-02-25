package pt.pmrelvas.pmr_expense_tracker.payloads.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "Error response payload")
@Builder(toBuilder = true)
public record ErrorApiResponsePayload(
    int errorCode,
    String message,
    List<Field> fields
) {
    @Builder(toBuilder = true)
    public record Field(
            String name,
            String value
    ) {
    }
}
