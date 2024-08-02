package com.psc.cajutest.dto.response;

public enum ResponseCode {
    APPROVED("00"),
    INSUFFICIENT_FUNDS("51"),
    ERROR("07");

    private final String code;

    ResponseCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
