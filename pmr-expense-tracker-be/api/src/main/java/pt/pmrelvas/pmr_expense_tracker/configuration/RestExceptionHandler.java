package pt.pmrelvas.pmr_expense_tracker.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pt.pmrelvas.pmr_expense_tracker.exceptions.NotFoundException;
import pt.pmrelvas.pmr_expense_tracker.payloads.response.ErrorApiResponsePayload;

@RestControllerAdvice
public class RestExceptionHandler {

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
}
