package com.transsion.authenticationsdk.infrastructure.constants;

/**
 * @Description: 衍生密钥托管方
 * @Author jiakang.chen
 * @Date 2023/4/18
 */
public enum TrusteeEnum {
    KMS(0, "KMS 进行密钥托管"),
    ONESELF(1, "业务方自己 进行密钥托管");

    /**
     * 托管方 code
     */
    private final Integer code;
    /**
     * 托管方 描述
     */
    private final String trusteeDescribe;

    TrusteeEnum(Integer code, String trusteeDescribe) {
        this.code = code;
        this.trusteeDescribe = trusteeDescribe;
    }

    public Integer getCode() {
        return code;
    }

    public String getTrusteeDescribe() {
        return trusteeDescribe;
    }

    /**
     * 通过 Code获取托管方
     *
     * @param code
     * @return
     */
    public TrusteeEnum getTrusteeForCode(Integer code) {
        for (TrusteeEnum value : TrusteeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
