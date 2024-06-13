package com.transsion.authentication.module.auth.bean.resp;

import lombok.Data;

/**
 * @Description: 衍生密钥 返回体
 * @Author jiakang.chen
 * @Date 2023/7/7
 */
@Data
public class DerivedKeyResp {
    /**
     * 衍生对称密钥 托管场景
     */
    @Data
    public static class SymmetryKmsScene {
        private String appSocketKey;
        private String randomNumber;
        private String appSocketKeySign;
    }

    /**
     * 衍生非对称密钥 非托管场景
     */
    @Data
    public static class SymmetryScene {
        private String appSocketKey;
        private String randomNumber;
        private String appSocketKeySign;
        private String severSocketKey;
    }

    /**
     * 衍生非对称密钥 托管场景
     */
    @Data
    public static class AsymmetryKmsScene {
        private String serverPublicKey;
        private String randomNumber;
        private String serverPublicKeySign;
    }

    /**
     * 衍生非对称密钥 非托管场景
     */
    @Data
    public static class AsymmetryScene {
        private String appPublicKey;
    }
}
