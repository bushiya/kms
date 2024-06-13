package com.transsion.authenticationfactory.infrastructure.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 接口统一返回码
 * @Author jiakang.chen
 * @Date 2023/2/6
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
     * 系统异常
     */
    SYSTEM_ERROR(500, "系统异常"),
    /**
     * 操作频繁，请稍后重试
     */
    FREQUENTLY_REQUEST(515, "操作频繁，请稍后重试"),
    /**
     * 请求头异常
     */
    HEADERS_VERIFY_FAIL(4000, "请求头异常"),
    /**
     * 证书不合法
     */
    CERT_ERROR(4001, "证书不合法"),
    /**
     * MES 服务异常
     */
    MES_ERROR(4002, "MES服务异常"),
    /**
     * 证书记录异常
     */
    CERT_RECORD_ERROR(4003, "证书记录异常"),


    END(-1, "异常码结束");

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
