package org.larin.budget.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends ApiException {
    public ForbiddenException(String s) {
        super(s);
    }
}
