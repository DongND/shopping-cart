package com.icommerce.shoppingapi.exception;

public class CartAlreadyConfirmedException extends RuntimeException {

    private static final long serialVersionUID = -1222949377638560540L;

    public CartAlreadyConfirmedException(final String message) {
        super(message);
    }
}
