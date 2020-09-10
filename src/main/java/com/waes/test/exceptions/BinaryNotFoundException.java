package com.waes.test.exceptions;

public class BinaryNotFoundException extends RuntimeException {

    public BinaryNotFoundException(String message, Object... args) {
        super(String.format(message.replace("{}", "%s"), args));
    }

}
