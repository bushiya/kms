package com.transsion.testtool.infrastructure.constants;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/9/8
 */
public enum KmsRealizeEnum {
    STANDARD_REALIZE("tee", "Kms系统标准实现,用于已导入Kms系统的"),
    COMPATIBILITY_REALIZE("sw", "AES 对称加密");

    /**
     * 实现标志
     */
    private final String realizeTag;
    /**
     * 实现描述
     */
    private final String realizeDescribe;

    KmsRealizeEnum(String realizeTag, String realizeDescribe) {
        this.realizeTag = realizeTag;
        this.realizeDescribe = realizeDescribe;
    }

    public String getRealizeTag() {
        return realizeTag;
    }

    public String getRealizeDescribe() {
        return realizeDescribe;
    }
}
