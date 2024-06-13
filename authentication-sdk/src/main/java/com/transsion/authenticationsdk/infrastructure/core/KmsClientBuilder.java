package com.transsion.authenticationsdk.infrastructure.core;

/**
 * @Description: KmsClient 对象 builder 方式创建
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class KmsClientBuilder {
    private KmsClient configData = new KmsClient();

    public static KmsClientBuilder builder() {
        return new KmsClientBuilder();
    }

    public static KmsClientBuilder builder(String appId, String url) {
        return new KmsClientBuilder();
    }

    public KmsClientBuilder appId(String appId) {
        configData.setAppId(appId);
        return this;
    }

    public KmsClientBuilder kmsUrl(String url) {
        configData.setKmsUrl(url);
        return this;
    }

    public KmsClientBuilder publicKey(String publicKey) {
        configData.setPublicKey(publicKey);
        return this;
    }

    public KmsClientBuilder privateKey(String privateKey) {
        configData.setPrivateKey(privateKey);
        return this;
    }

    public KmsClientBuilder kmsPublicKey(String kmsPublicKey) {
        configData.setKmsPublicKey(kmsPublicKey);
        return this;
    }

    public KmsClient build() {
        return this.configData;
    }
}
