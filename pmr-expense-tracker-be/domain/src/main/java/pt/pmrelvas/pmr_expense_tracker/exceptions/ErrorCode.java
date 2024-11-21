package pt.pmrelvas.pmr_expense_tracker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    CATEGORY_NOT_FOUND(4000);

    private int code;

}
