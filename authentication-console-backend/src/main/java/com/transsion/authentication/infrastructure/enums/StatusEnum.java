package com.transsion.authentication.infrastructure.enums;

/**
 * 起停用 : 1.禁用  2.启用
 */
public enum StatusEnum {

    DISABLE(1, "禁用"),
    ENABLE(2, "启用");

    private Integer code;
    private String message;

    StatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
