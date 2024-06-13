package com.transsion.authenticationfactory.module.device.service;

import com.transsion.authenticationfactory.module.device.bean.resp.DeviceCertResp;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
public interface DeviceAuthService {
    /**
     * 获取设备证书
     *
     * @param csr 证书请求文件
     * @return
     */
    DeviceCertResp getDeviceCert(String csr) throws Exception;
}
