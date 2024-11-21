package pt.pmrelvas.pmr_expense_tracker.exceptions;

public class NotFoundException extends CodedException {

    private NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static NotFoundException buildForCategory(long id) {
        return new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND, "Category with id %s not found".formatted(id));
    }
}
