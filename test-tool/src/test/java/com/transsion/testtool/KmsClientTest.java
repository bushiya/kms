package com.transsion.testtool;

import com.transsion.authenticationsdk.infrastructure.core.KmsClient;
import com.transsion.testtool.infrastructure.constants.KmsRealizeEnum;
import com.transsion.testtool.infrastructure.utils.AESUtil;
import com.transsion.testtool.infrastructure.utils.RSAUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/9/8
 */
@SpringBootTest
public class KmsClientTest {
    @Autowired
    KmsClient kmsClient;

    @Autowired
    KmsClient kmsCompatibilityClient;

    @Test
    void testEncrypt() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String encrypt = kmsClient.encrypt(sessionId, "default", "123");
            System.out.println(encrypt);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String encrypt = kmsCompatibilityClient.encrypt(compatibilitySessionId, "default", "123");
            System.out.println(encrypt);
        }
    }

    @Test
    void testDecrypt() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String encrypt = "客户端加密数据的密文";
            String decrypt = kmsClient.decrypt(sessionId, encrypt);
            System.out.println(decrypt);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String encrypt = "客户端加密数据的密文";
            String decrypt = kmsCompatibilityClient.decrypt(compatibilitySessionId, encrypt);
            System.out.println(decrypt);
        }
    }


    @Test
    void testSign() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String data = "123";
            String sign = kmsClient.sign(sessionId, "testRSA", data);
            System.out.println(sign);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String data = "123";
            String sign = kmsCompatibilityClient.sign(compatibilitySessionId, "testRSA", data);
            System.out.println(sign);
        }
    }

    @Test
    void testVerify() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String data = "数据";
            String sign = "客户端对数据的签名值";
            boolean verify = kmsClient.verify(sessionId, "testRSA", data, sign);
            System.out.println(verify);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String data = "数据";
            String sign = "客户端对数据的签名值";
            boolean verify = kmsCompatibilityClient.verify(compatibilitySessionId, "testRSA", data, sign);
            System.out.println(verify);
        }
    }

    @Test
    void testDirvate() {
        // 设备单个应用的标识,业务方自己维护
        String sessionId = "7A3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 标注实现
        String standardRealize = "tee";
        if (standardRealize.equals(KmsRealizeEnum.STANDARD_REALIZE.getRealizeTag())) {
            String data = "数据";
            String sign = "客户端对数据的签名值";
//            boolean verify = kmsClient.derivedSecretKey(sessionId, "123");
//            System.out.println(verify);
        }
        // 设备单个应用的标识,业务方自己维护
        String compatibilitySessionId = "8D3D818FB357D288AF4147450E705EAE8096307991279E290708A5FB7751535E";
        // 兼容实现
        String compatibilityRealize = "sw";
        if (compatibilityRealize.equals(KmsRealizeEnum.COMPATIBILITY_REALIZE.getRealizeTag())) {
            String data = "数据";
            String sign = "客户端对数据的签名值";
            boolean verify = kmsCompatibilityClient.verify(compatibilitySessionId, "testRSA", data, sign);
            System.out.println(verify);
        }
    }

    public static void main(String[] args) throws Exception {
        String publicKey = AESUtil.decrypt("lhJGMB/jCUXEScw0DpHWlji8nX28/BuuWSds8pUYc6ZU3jA4qz1XAyfXCJ3Ze8xfJCf6EmoSB7ET0Dzn3nUiXXIGCOqk4wMYeqSbfGQmdOZLCIpNd6XpRNDOktGD4/zyoThEwD4xtkCYMepJ6+ORrIjOJXBQe2o/wfBrWMLK6GpT0Qx/01lZrFzS9e5h3Y/l3FgcRrrySyVosuypp28fiGoe4WRkMbbLj3BkoyHRGJRK2ScGyPdzt9X1VdJZr2RlQO4VLRUx2oqkhsDDJshuOSD5b2/ibJUGH7r8d8q2iIpUFkPzquQ/ekxYozVJ7eNFX3AlWhdQFEGdDnyhmEu3qPW9sfEOYh27VbW7hyPZRPn9FflYbZJ19nNyBKI/5nHhc6/v3OkGQk+9uGV7U8OzJ/BD3gIwB8fMUMPH5nEjaxt7rJVyVN2VBW9Bqz/8YXG8dQBdAS+p0WFYGlPFRav/ma+LJyd2tBF6ihfeKb8fue0Z3NQvejHkMljqmTDTud8Lr4fnWWT5Ed0jop9nXOgiUw==", "6XYVtyvgwyT5TGc0");
        System.out.println(publicKey);
        RSAUtil.verify("123", publicKey, "V992qGJB5hV5GJSnskJJjmctoPW6xCZr+FQRhDADud27mxaiivklgm8p0wO+VlA6K8iGR0SdHD5pM9xr0BO80sTvKPvf3DDj5+W6Di55k5slkRLLzJUEbjQxdLfHG2Ru6Ia9+U+fx+AxVvZyRpjgs/MhYANGnkprJY7a0o8T6BM8d3Ww0xaTcIInn/QPMhznLZmrJ7C59Ttdw9vnQr5aXBXYVFA5RbgXF9NrNvODRnzOV1J5815gDbGQqY+tk+CeE/svd18oTu5x5VUd/iijX171GWrlK9XRE048MuPvT3+QFOJrCfA8n2i4xm/jIfmwNWd4dxeWwTeFFPqDeODDIw==");
    }
}
