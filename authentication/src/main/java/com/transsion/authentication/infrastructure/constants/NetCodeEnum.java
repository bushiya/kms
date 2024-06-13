package com.transsion.authentication.infrastructure.constants;

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
     * 身份异常
     */
    IDENTITY_FAIL(503, "身份异常"),
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

    /**
     * 存储密钥异常
     */
    STORAGE_KEY_ERROR(4004, "存储密钥异常"),
    /**
     * 业务方不存在
     */
    CUT_IN_ERROR(4005, "业务方不存在"),
    /**
     * 生成密钥异常
     */
    GENERATE_SECRET_KEY_ERROR(4006, "生成密钥异常"),
    /**
     * 密钥过期异常
     */
    STORAGE_KEY_PAST_DUE_ERROR(4007, "密钥过期"),
    /**
     * IP 不合法
     */
    IP_ERROR(4008, "IP不合法"),
    /**
     * 密钥不存在
     */
    STORAGE_KEY_NOT_ERROR(4009, "密钥不存在"),
    /**
     * 不支持此算法
     */
    ALGORITHM_ERROR(4010, "不支持此算法"),
    /**
     * 该场景密钥不支持此操作
     */
    SCENE_USE_ERROR(4011, "该场景密钥不支持此操作"),
    /**
     * 业务方场景初始化异常
     */
    SECRET_KEY_SCENE_SAVE_ERROR(4012, "业务方场景初始化异常"),
    /**
     * 衍生密钥存储异常
     */
    SECRET_KEY_SAVE_ERROR(4012, "衍生密钥存储异常"),
    /**
     * 生成密钥错误
     */
    GENERATE_STORAGE_KEY_ERROR(4013, "生成密钥错误"),
    /**
     * 托管方异常
     */
    TRUSTEE_ERROR(4014, "托管方式异常"),
    /**
     * App 初始化异常
     */
    APP_INIT_ERROR(4015, "App初始化异常"),
    /**
     * 业务方未接入
     */
    APP_IN_USER_ERROR(4016, "业务方未接入"),
    /**
     * 业务方不存在
     */
    SDK_ENCODE_ERROR(4777, "SDK 加密措施缺失"),
    /**
     * 检测到重放攻击
     */
    REPLAY_ATTACK_ERROR(4888, "检测到重放攻击"),
    /**
     * 云关闭
     */
    CLOUD_CLOSE_ERROR(4999, "云关闭"),

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
