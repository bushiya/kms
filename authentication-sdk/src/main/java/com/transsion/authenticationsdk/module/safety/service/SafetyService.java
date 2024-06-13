package com.transsion.authenticationsdk.module.safety.service;

import com.transsion.authenticationsdk.infrastructure.core.KmsClient;
import com.transsion.authenticationsdk.module.safety.body.*;

/**
 * @Description: KMS 通信安全 功能模块
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public interface SafetyService {
    /**
     * 加密消息
     *
     * @param kmsClient 业务方身份信息
     * @param req       加密请求体
     * @return
     */
    String encrypt(KmsClient kmsClient, EncryptReq req);

    /**
     * 解密消息
     *
     * @param kmsClient 业务方身份信息
     * @param req       解密请求体
     * @return
     */
    String decrypt(KmsClient kmsClient, DecryptReq req);

    /**
     * 签名消息
     *
     * @param kmsClient 业务方身份信息
     * @param req       签名请求体
     * @return
     */
    String sign(KmsClient kmsClient, SignReq req);

    /**
     * 验签消息
     *
     * @param kmsClient 业务方身份信息
     * @param req       验签请求体
     * @return
     */
    boolean verify(KmsClient kmsClient, VerifyReq req);

    /**
     * 衍生密钥
     *
     * @param kmsClient 业务方身份信息
     * @param req       衍生密钥请求体
     * @return
     */
    String derivedKey(KmsClient kmsClient, DerivedKey req);
}
