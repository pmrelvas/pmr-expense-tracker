package pt.pmrelvas.pmr_expense_tracker.exceptions;

import java.util.Map;

public class NotFoundException extends CodedException {

    private NotFoundException(ErrorCode errorCode, String message, Map<String, Object> fields) {
        super(errorCode, message, fields);
    }

    public static NotFoundException buildForCategory(long id) {
        return new NotFoundException(
                ErrorCode.CATEGORY_NOT_FOUND,
                "Category with id %s not found".formatted(id),
                Map.of("id", id));
    }
}
