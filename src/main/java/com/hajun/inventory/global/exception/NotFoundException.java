package com.hajun.inventory.common.exception;

public class NotFoundException extends RuntimeException {
    private String code;

    public NotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
    public NotFoundException(String message) {
        super(message);
    }


    public String getCode() {
        return code;
    }

}
