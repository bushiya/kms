package com.transsion.authenticationsdk.infrastructure.constants;

/**
 * @Description: KMS 服务接口路径常量
 * @Author jiakang.chen
 * @Date 2023/7/13
 */
public class RequestUriConst {
    /**
     * 加密
     */
    public static final String INIT = "/server/init";
    /**
     * 加密
     */
    public static final String ENCRYPT = "/device/encode-message";
    /**
     * 解密
     */
    public static final String DECRYPT = "/device/decode-message";
    /**
     * 签名
     */
    public static final String SIGN = "/device/sign-message";
    /**
     * 验签
     */
    public static final String VERIFY = "/device/verify-sign";
    /**
     * 衍生密钥
     */
    public static final String DERIVED = "/device/derived-key";
}
