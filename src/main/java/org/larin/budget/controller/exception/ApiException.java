package org.larin.budget.controller.exception;


public class ApiException extends RuntimeException {
    public ApiException() {
        super();
    }

    public ApiException(String s) {
        super(s);
    }

    public ApiException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }
}
