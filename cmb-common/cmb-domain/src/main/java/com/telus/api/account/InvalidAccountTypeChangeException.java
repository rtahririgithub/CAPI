package com.telus.api.account;

import com.telus.api.TelusAPIException;

public class InvalidAccountTypeChangeException extends TelusAPIException {
    public InvalidAccountTypeChangeException(String message, Throwable exception) {
        super(message, exception);
    }

    public InvalidAccountTypeChangeException(Throwable exception) {
        super(exception);
    }

    public InvalidAccountTypeChangeException(String message) {
        super(message);
    }
}
