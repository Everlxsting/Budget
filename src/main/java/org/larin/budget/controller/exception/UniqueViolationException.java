package org.larin.budget.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UniqueViolationException extends ApiException {
    public UniqueViolationException(String s) {
        super(s);
    }
}
