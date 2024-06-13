package com.transsion.authenticationsdk.module.auth.body;

/**
 * @Description: 与 KMS 服务器建立安全通道 返回体
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class ServerAuthResp {
    /**
     * 公钥加密后的 通信对称密钥
     */
    private String secretKey;
    /**
     * 签名信息
     */
    private String sign;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
