package com.telus.api.account;

import com.telus.api.TelusAPIException;

public class AddressNotFoundException extends TelusAPIException {
    public AddressNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public AddressNotFoundException(Throwable exception) {
        super(exception);
    }

    public AddressNotFoundException(String message) {
        super(message);
    }
}
