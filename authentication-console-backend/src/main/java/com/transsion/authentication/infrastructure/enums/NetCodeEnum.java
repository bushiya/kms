package com.transsion.authentication.infrastructure.enums;

import org.apache.commons.lang3.StringUtils;

public enum NetCodeEnum {
    //Basic Error
    SUCCESS(0, "Success"),
    PARAM_VERIFY_FAIL(400, "Request parameter error"),
    SYSTEM_ERROR(500, "System error"),
    SIGN_ERROR(40001, "verify sign failed"),
    KEY_ERROR(40002, "Key is error"),
    SAVE_KEY_ERROR(40003, "Save Key error"),
    DEVICE_NOT_TRUSTED(40004, "Device not trusted"),
    HAVE_NOT_MAIN_KEY(40005, "Main key is empty"),
    HAVE_NOT_MSG_KEY(40006, "Main key is empty"),
    ACCESS_DENIED(40007, "Access denied"),
    /**
     * 验签失败
     */
    SIGN_VERIFICATION_FAILED(405, "验签失败"),


    /**
     * KMS 管理后台 1004 + 错误码
     */
    APP_ID_DATA_ALREADY_EXISTS(10040, "应用ID存在"),
    APP_NAME_DATA_ALREADY_EXISTS(10043, "应用名称存在"),
    APP_PACKAGE_DATA_ALREADY_EXISTS(10044, "应用包名存在"),
    DATA_ALREADY_NOT_EXISTS(10041, "数据不存在"),

    BLACKLIST_NAME_DATA_ALREADY_EXISTS(100401, "黑名单证书名称存在"),
    BLACKLIST_CERT_VALUE_DATA_ALREADY_EXISTS(100402, "黑名单证书值存在"),

    BLACKLIST_MODEL_NAME_AND_MODEL_VALUE_SAME(100403, "黑名单机型名称及机型值相同"),
    BLACKLIST_MODEL_VALUE_DATA_ALREADY_EXISTS(100404, "黑名单机型值存在"),

    DEFAULT(-1, "");
    private final Integer code;
    private final String messageKey;

    NetCodeEnum(Integer code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public static NetCodeEnum getEnumByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            for (NetCodeEnum e : values()) {
                if (StringUtils.equalsIgnoreCase(name, e.name())) {
                    return e;
                }
            }
        }
        return SYSTEM_ERROR;
    }


    public static NetCodeEnum getEnumByCode(Integer code) {
        if (code != null) {
            for (NetCodeEnum e : values()) {
                if (e.getCode() == code.intValue()) {
                    return e;
                }
            }
        }
        return SYSTEM_ERROR;
    }
}
