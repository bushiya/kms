package com.transsion.authentication.module.auth.service;

/**
 * @Description: MES 服务接口
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
public interface MesAuthService {
    /**
     * 获取认证服务证书
     *
     * @param csr 证书请求文件
     * @return
     */
    String getAuthCert(String csr);

    /**
     * 获取认证服务存储密钥
     *
     * @param randomNumber 随机数
     * @return
     */
    String getStorageKey(String randomNumber);
}
