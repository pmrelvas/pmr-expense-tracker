package pt.pmrelvas.pmr_expense_tracker.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pt.pmrelvas.pmr_expense_tracker.exceptions.ErrorCode;
import pt.pmrelvas.pmr_expense_tracker.exceptions.NotFoundException;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.ErrorApiResponsePayload;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorApiResponsePayload> handleNotFoundException(NotFoundException e) {
        ErrorApiResponsePayload responsePayload = ErrorApiResponsePayload.builder()
                .errorCode(e.getErrorCode().getCode())
                .message(e.getMessage())
                .fields(e.getFields().entrySet().stream()
                        .map((entry) -> ErrorApiResponsePayload.Field.builder()
                                .name(entry.getKey())
                                .value(String.valueOf(entry.getValue()))
                                .build())
                        .toList()
                )
                .build();
        return new ResponseEntity<>(responsePayload, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponsePayload> handleGenericException(Exception e) {
        log.error("Unhandled exception while processing request", e);
        ErrorApiResponsePayload responsePayload = ErrorApiResponsePayload.builder()
                .errorCode(ErrorCode.INTERNAL_ERROR.getCode())
                .message("An error has occurred")
                .build();
        return new ResponseEntity<>(responsePayload, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
