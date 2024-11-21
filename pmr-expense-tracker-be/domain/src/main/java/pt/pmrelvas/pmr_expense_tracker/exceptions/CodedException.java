package pt.pmrelvas.pmr_expense_tracker.exceptions;

public abstract class CodedException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;
    private final Throwable cause;

    protected CodedException(ErrorCode errorCode, String message, Throwable cause) {
        this.errorCode = errorCode;
        this.message = message;
        this.cause = cause;
    }

    protected CodedException(ErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }
}
