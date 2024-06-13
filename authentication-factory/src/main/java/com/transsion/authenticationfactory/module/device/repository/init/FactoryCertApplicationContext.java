package com.transsion.authenticationfactory.module.device.repository.init;

import cn.hutool.core.codec.Base64Encoder;
import com.transsion.authenticationfactory.infrastructure.utils.CertUtil;
import com.transsion.authenticationfactory.infrastructure.utils.CsrUtil;
import com.transsion.authenticationfactory.infrastructure.utils.RsaByteUtil;
import com.transsion.authenticationfactory.module.device.repository.resource.RootCertResource;
import com.transsion.authenticationfactory.module.device.service.MesAuthService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Slf4j
@Component
public class FactoryCertApplicationContext implements ApplicationContextAware {

    @Autowired
    MesAuthService mesAuthService;

    @Autowired
    RootCertResource rootCertResource;

    @Autowired
    CertUtil certUtil;

    @Autowired
    CsrUtil csrUtil;

    /**
     * 公钥 私钥
     */
    private static Map<String, byte[]> factoryRsa = new HashMap<>(2);
    /**
     * 工厂证书
     */
    private static X509Certificate factoryCert = null;
    /**
     * 根证书
     */
    private static X509Certificate rootCert = null;


    @SneakyThrows
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 工厂证书转换
        rootCert = certUtil.getCertByStr(rootCertResource.getRootCert());
        log.info("根证书初始化完成");
        // 初始化 RSA 密钥
        Map<String, byte[]> map = RsaByteUtil.initRSAKey(2048);
        factoryRsa.putAll(map);
        log.info("工厂密钥初始化完成");
        String factoryCsr = csrUtil.generateCsrString(getPublicKey(), getPrivateKey());
        // 调用MES服务获取工厂公钥
        String cert = mesAuthService.getFactoryCert(factoryCsr);
        factoryCert = certUtil.getCertByStr(cert);
        log.info("工厂证书获取完成");
        // 验证工厂证书
        factoryCert.checkValidity();
        factoryCert.verify(rootCert.getPublicKey());
        log.info("工厂证书验证完成");
    }

    /**
     * 获取私钥
     *
     * @return
     */
    public RSAPrivateKey getPrivateKey() {
        assert !ObjectUtils.isEmpty(factoryRsa.get("privateKey")) : "初始化RSA密钥失败";
        return RsaByteUtil.strByPrivateKey(factoryRsa.get("privateKey"));
    }

    /**
     * 获取公钥
     *
     * @return
     */
    public RSAPublicKey getPublicKey() {
        assert !ObjectUtils.isEmpty(factoryRsa.get("publicKey")) : "初始化RSA密钥失败";
        return RsaByteUtil.strByPublicKey(factoryRsa.get("publicKey"));
    }

    /**
     * 获取工厂证书
     *
     * @return
     */
    public X509Certificate getFactoryCert() {
        assert !ObjectUtils.isEmpty(factoryCert) : "初始化工厂证书失败";
        return factoryCert;
    }

    /**
     * 获取工厂证书字符串
     *
     * @return
     */
    public String getFactoryCertStr() throws CertificateEncodingException {
        assert !ObjectUtils.isEmpty(factoryCert) : "初始化工厂证书失败";
        return Base64Encoder.encode(factoryCert.getEncoded());
    }

}
