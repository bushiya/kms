package com.transsion.authenticationsdk.infrastructure.constants;

/**
 * @Description: 衍生密钥算法枚举
 * @Author jiakang.chen
 * @Date 2023/4/18
 */
public enum AlgorithmEnum {
    RSA(0, "RSA 非对称"),
    AES(1, "AES 对称加密");

    /**
     * 算法传输 code
     */
    private final Integer code;
    /**
     * 算法描述
     */
    private final String algorithmDescribe;

    AlgorithmEnum(Integer code, String algorithmDescribe) {
        this.code = code;
        this.algorithmDescribe = algorithmDescribe;
    }

    public Integer getCode() {
        return code;
    }

    public String getAlgorithmDescribe() {
        return algorithmDescribe;
    }

    /**
     * 通过 Code获取算法枚举
     *
     * @param code
     * @return
     */
    public AlgorithmEnum getAlgorithmForCode(Integer code) {
        for (AlgorithmEnum value : AlgorithmEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
