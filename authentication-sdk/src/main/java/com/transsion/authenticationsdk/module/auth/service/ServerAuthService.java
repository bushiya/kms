package com.transsion.authenticationsdk.module.auth.service;

import com.transsion.authenticationsdk.infrastructure.core.KmsClient;

/**
 * @Description: 与 KMS 服务器建立安全通道
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public interface ServerAuthService {
    /**
     * 与 KMS 服务器建立安全通道
     *
     * @param kmsClient 业务方核心参数
     * @return
     */
    KmsClient initServer(KmsClient kmsClient);
}
