package com.hajun.inventory.common.exception;

public class ConflictException extends RuntimeException {
    private final String code;

    public ConflictException(String code, String message) {
        super(message);
        this.code = code;
    }
    public ConflictException(String message) {
        super(message);
        this.code = "CONFLICT";  // 기본 코드
    }


    public String getCode() {
        return code;
    }
}
