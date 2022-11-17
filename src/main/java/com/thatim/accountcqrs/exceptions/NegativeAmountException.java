package com.thatim.accountcqrs.exceptions;

public class NegativeAmountException extends Throwable {
    public NegativeAmountException(String message) {
        super(message);
    }
}
