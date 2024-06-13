package com.transsion.authentication.infrastructure.utils;


import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @Description: 证书请求文件工具类
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Component
public class CsrUtil implements ApplicationContextAware {
    // 名称
    private final String CN = "KMS Auth Compatibility CA";
    // 国家
    private final String C = "CN";
    // 省份
    private final String ST = "ShangHai";
    // 城市
    private final String L = "ShangHai";
    // 组织
    private final String O = "kms.transsion-os.com";
    // 组织单位
    private String OU = "transsion";
    // 证书请求文件
    private X500Name csr;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        X500NameBuilder localX500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        localX500NameBuilder.addRDN(BCStyle.CN, CN);
        localX500NameBuilder.addRDN(BCStyle.C, C);
        localX500NameBuilder.addRDN(BCStyle.ST, ST);
        localX500NameBuilder.addRDN(BCStyle.L, L);
        localX500NameBuilder.addRDN(BCStyle.O, O);
        localX500NameBuilder.addRDN(BCStyle.OU, OU);
        csr = localX500NameBuilder.build();
    }

    /**
     * 获取证书请求字符串
     *
     * @param publicKey  请求者公钥
     * @param privateKey 请求者私钥
     * @return
     * @throws Exception
     */
    public String generateCsrString(PublicKey publicKey, PrivateKey privateKey) throws Exception {
        // 使用私钥和 SHA256WithRSA/SM3withSM2 算法创建签名者对象
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);
        // 创建 CSR
        PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(csr, publicKey);
        PKCS10CertificationRequest csr = builder.build(signer);
        return Base64Encoder.encode(csr.getEncoded());
    }

    /**
     * 将 csr 字符串转为 证书请求对象
     *
     * @param csrString 证书请求字符串
     * @return
     * @throws Exception
     */
    public PKCS10CertificationRequest getCsrObject(String csrString) throws Exception {
        PKCS10CertificationRequest pkcs10CertificationRequest = new PKCS10CertificationRequest(Base64Decoder.decode(csrString));
        // 将主题名称转换为 PKCS10CertificationRequest 对象
        return pkcs10CertificationRequest;
    }

    /**
     * 获取 csr 中的公钥
     *
     * @param csr 证书请求对象
     * @return
     * @throws Exception
     */
    public RSAPublicKey getPublicKeyByCsr(PKCS10CertificationRequest csr) throws Exception {
        SubjectPublicKeyInfo subjectPublicKeyInfo = csr.getSubjectPublicKeyInfo();
        RSAPublicKey rsaPublicKey = RsaByteUtil.strByPublicKey(subjectPublicKeyInfo.getEncoded());
        // 将主题名称转换为 X500Principal 对象
        return rsaPublicKey;
    }

    /**
     * 获取 csr 中的公钥
     *
     * @param csr 证书请求字符串
     * @return
     * @throws Exception
     */
    public RSAPublicKey getPublicKeyByCsr(String csr) throws Exception {
        PKCS10CertificationRequest pkcs10CertificationRequest = getCsrObject(csr);
        // 将主题名称转换为 X500Principal 对象
        return getPublicKeyByCsr(pkcs10CertificationRequest);
    }

    /**
     * 打印 证书请求文件
     *
     * @param csr
     * @return
     * @throws IOException
     */
    public void printOpensslPemFormatCsrFileContent(PKCS10CertificationRequest csr) throws IOException {
        PemObject pem = new PemObject("CERTIFICATE REQUEST", csr.getEncoded());
        StringWriter str = new StringWriter();
        PemWriter pemWriter = new PemWriter(str);
        pemWriter.writeObject(pem);
        pemWriter.close();
        str.close();
        System.out.println(str.toString());
    }

    /**
     * 打印 证书请求文件
     *
     * @param csr
     * @return
     * @throws IOException
     */
    public void printOpensslPemFormatCsrFileContent(String csr) throws Exception {
        PKCS10CertificationRequest pkcs10CertificationRequest = getCsrObject(csr);
        PemObject pem = new PemObject("CERTIFICATE REQUEST", pkcs10CertificationRequest.getEncoded());
        StringWriter str = new StringWriter();
        PemWriter pemWriter = new PemWriter(str);
        pemWriter.writeObject(pem);
        pemWriter.close();
        str.close();
        System.out.println(str.toString());
    }


}

