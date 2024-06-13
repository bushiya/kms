package com.transsion.authenticationfactory.module.device.service;

/**
 * @Description: MES 服务接口
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
public interface MesAuthService {
    /**
     * 获取工厂证书
     * @param csr 证书请求文件
     * @return
     */
    String getFactoryCert(String csr);
}
