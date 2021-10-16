package com.icommerce.shoppingapi.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
