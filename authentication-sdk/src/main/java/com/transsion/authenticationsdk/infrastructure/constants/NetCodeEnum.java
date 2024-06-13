package com.transsion.authenticationsdk.infrastructure.constants;

/**
 * @Description: 错误码
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
public enum NetCodeEnum {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),
    /**
     * 参数异常
     */
    PARAM_VERIFY_FAIL(400, "参数异常"),
    /**
     * 用户无权限
     */
    USER_NOT_PERMISSION(401, "用户无权限"),
    /**
     * 登录异常
     */
    LOGIN_OUT(403, "登录异常"),
    /**
     * 验签失败
     */
    SIGN_VERIFICATION_FAILED(405, "验签失败"),
    /**
     * 解密失败
     */
    DECRYPTION_FAILURE(406, "解密失败"),
    /**
     * 加密失败
     */
    ENCRYPTION_FAILURE(407, "加密失败"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(500, "系统异常"),
    /**
     * 操作频繁，请稍后重试
     */
    FREQUENTLY_REQUEST(515, "操作频繁，请稍后重试"),
    /**
     * 操作频繁，请稍后重试
     */
    KMS_AUTH_CHANNEL(999, "与KMS建立安全通道失败"),
    /**
     * 密钥过期异常
     */
    STORAGE_KEY_PAST_DUE_ERROR(4007, "密钥过期"),

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
