package com.transsion.authentication.module.auth.repository.init;

import cn.hutool.core.codec.Base64Encoder;
import com.transsion.authentication.infrastructure.utils.CertUtil;
import com.transsion.authentication.infrastructure.utils.CsrUtil;
import com.transsion.authentication.infrastructure.utils.RsaByteUtil;
import com.transsion.authentication.module.auth.repository.resource.RootCertResource;
import com.transsion.authentication.module.auth.service.MesAuthService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 初始化公网证书
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Slf4j
@Component
public class AuthCertApplicationContext implements ApplicationContextAware {

    @Autowired
    MesAuthService mesAuthService;

    @Autowired
    RootCertResource rootCertResource;

    @Autowired
    CsrUtil csrUtil;

    @Autowired
    CertUtil certUtil;

    /**
     * 公钥 私钥
     */
    private static Map<String, byte[]> authRsa = new HashMap<>(2);
    /**
     * 工厂证书
     */
    private static X509Certificate authCert = null;
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
        authRsa.putAll(map);
        log.info("公网密钥初始化完成");
        String factoryCsr = csrUtil.generateCsrString(getPublicKey(), getPrivateKey());
        // 调用MES服务获取工厂公钥
        String cert = mesAuthService.getAuthCert(factoryCsr);
        log.info("证书:{}", cert);
        authCert = certUtil.getCertByStr(cert);
        log.info("公网证书获取完成");
        // 验证工厂证书
        authCert.checkValidity();
        authCert.verify(rootCert.getPublicKey());
        log.info("公网证书验证完成");
    }

    /**
     * 获取私钥
     *
     * @return
     */
    public RSAPrivateKey getPrivateKey() {
        assert !ObjectUtils.isEmpty(authRsa.get("privateKey")) : "初始化RSA密钥失败";
        return RsaByteUtil.strByPrivateKey(authRsa.get("privateKey"));
    }

    /**
     * 获取公钥
     *
     * @return
     */
    public RSAPublicKey getPublicKey() {
        assert !ObjectUtils.isEmpty(authRsa.get("publicKey")) : "初始化RSA密钥失败";
        return RsaByteUtil.strByPublicKey(authRsa.get("publicKey"));
    }

    /**
     * 获取公网证书
     *
     * @return
     */
    public X509Certificate getAuthCert() {
        assert !ObjectUtils.isEmpty(authCert) : "初始化工厂证书失败";
        return authCert;
    }

    /**
     * 获取公网证书字符串
     *
     * @return
     */
    @SneakyThrows
    public String getAuthCertStr() {
        assert !ObjectUtils.isEmpty(authCert) : "初始化工厂证书失败";
        return Base64Encoder.encode(authCert.getEncoded());
    }

    /**
     * 获取根证书
     *
     * @return
     */
    public X509Certificate getRootCert() {
        assert !ObjectUtils.isEmpty(rootCert) : "初始化工厂证书失败";
        return rootCert;
    }

}
