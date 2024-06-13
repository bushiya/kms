package com.transsion.authenticationsdk;


import com.transsion.authenticationsdk.infrastructure.constants.KmsRealizeEnum;
import com.transsion.authenticationsdk.infrastructure.core.KmsClient;
import com.transsion.authenticationsdk.infrastructure.core.KmsClientBuilder;

public class DemoMain {

    /**
     * 业务 公钥
     */
    private static String sdkPublicKey = "";
    /**
     * 业务 私钥
     */
    private static String sdkPrivateKey = "";
    /**
     * 接入时返回的 KMS公钥
     */
    private static String serverPublicKey = "";
    /**
     * 接入时返回的 AppID
     */
    private static String appId = "";


    private static KmsClient kmsClient = KmsClientBuilder.builder().kmsUrl("https://test-kms.transsion-os.com/auth/").appId(appId)
            .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();

    private static KmsClient kmsCompatibilityClient = KmsClientBuilder.builder().kmsUrl("https://test-kms.transsion-os.com/compatibility/").appId(appId)
            .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();

    void testEncrypt() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现 业务方自己维护
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String encrypt = kmsClient.encrypt(sessionId, "default", "123");
            System.out.println(encrypt);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现 业务方自己维护
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String encrypt = kmsCompatibilityClient.encrypt(compatibilitySessionId, "default", "123");
            System.out.println(encrypt);
        }
    }

    void testDecrypt() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现 业务方自己维护
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String encrypt = "客户端加密数据的密文";
            String decrypt = kmsClient.decrypt(sessionId, encrypt);
            System.out.println(decrypt);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现 业务方自己维护
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String encrypt = "客户端加密数据的密文";
            String decrypt = kmsCompatibilityClient.decrypt(compatibilitySessionId, encrypt);
            System.out.println(decrypt);
        }
    }

    void testSign() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现 业务方自己维护
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String data = "123";
            String sign = kmsClient.sign(sessionId, "testRSA", data);
            System.out.println(sign);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现 业务方自己维护
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String data = "123";
            String sign = kmsCompatibilityClient.sign(compatibilitySessionId, "testRSA", data);
            System.out.println(sign);
        }
    }

    void testVerify() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现 业务方自己维护
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String data = "数据";
            String sign = "客户端对数据的签名值";
            boolean verify = kmsClient.verify(sessionId, "testRSA", data, sign);
            System.out.println(verify);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现 业务方自己维护
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String data = "数据";
            String sign = "客户端对数据的签名值";
            boolean verify = kmsCompatibilityClient.verify(compatibilitySessionId, "testRSA", data, sign);
            System.out.println(verify);
        }
    }
}
