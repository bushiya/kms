package com.transsion.authenticationfactory.module.device.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import com.transsion.authenticationfactory.infrastructure.utils.CertUtil;
import com.transsion.authenticationfactory.module.device.bean.resp.DeviceCertResp;
import com.transsion.authenticationfactory.module.device.repository.init.FactoryCertApplicationContext;
import com.transsion.authenticationfactory.module.device.service.DeviceAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;


/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Slf4j
@Service
public class DeviceAuthServiceImpl implements DeviceAuthService {

    @Autowired
    FactoryCertApplicationContext factoryCertApplicationContext;

    @Autowired
    CertUtil certUtil;

    /**
     * 获取设备公钥证书
     *
     * @param csr 证书请求文件
     * @return
     */
    @Override
    public DeviceCertResp getDeviceCert(String csr) throws Exception {
        // 使用工厂私钥 签发证书

        X509Certificate deviceCert = certUtil.generateCertByCsr(csr, factoryCertApplicationContext.getPrivateKey());
        deviceCert.verify(factoryCertApplicationContext.getPublicKey());
        // 返回
        DeviceCertResp resp = new DeviceCertResp();
        resp.setDeviceCert(Base64Encoder.encode(deviceCert.getEncoded()));
        resp.setFactoryCert(factoryCertApplicationContext.getFactoryCertStr());
        return resp;
    }
}
