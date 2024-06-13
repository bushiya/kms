package com.transsion.authentication.infrastructure.utils;

import cn.hutool.core.codec.Base64Decoder;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

/**
 * @Description: 证书工具类
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Component
public class CertUtil implements ApplicationContextAware {
    //签名算法
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    //生效时间
    private static final long START_DATE = 2 * 24 * 60 * 60 * 1000L;
    //有效时间 100 年
    private static final long END_DATE = 100L * 365 * 24 * 60 * 60 * 1000;

    // 名称
    private final String CN = "KMS Auth CA";
    // 国家
    private final String C = "CN";
    // 省份
    private final String ST = "ShangHai";
    // 城市
    private final String L = "ShangHai";
    // 组织
    private final String O = "kms.transsion-os.com";
    // 组织单位
    private static final String OU = "transsion";

    private static X500Name authCa = null;

    @Autowired
    CsrUtil csrUtil;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        X500NameBuilder localX500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        localX500NameBuilder.addRDN(BCStyle.CN, CN);
        localX500NameBuilder.addRDN(BCStyle.C, C);
        localX500NameBuilder.addRDN(BCStyle.ST, ST);
        localX500NameBuilder.addRDN(BCStyle.L, L);
        localX500NameBuilder.addRDN(BCStyle.O, O);
        localX500NameBuilder.addRDN(BCStyle.OU, OU);
        authCa = localX500NameBuilder.build();
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 签发证书
     *
     * @param csr        证书请求文件
     * @param privateKey
     * @return
     * @throws Exception
     */
    public X509Certificate generateCertByCsr(PKCS10CertificationRequest csr, PrivateKey privateKey) throws Exception {
        // 证书生效时间
        Date startDate = new Date(System.currentTimeMillis() - START_DATE);
        // 证书过期时间
        Date endDate = new Date(System.currentTimeMillis() + END_DATE);
        // 证书序列号
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
        RSAPublicKey publicKey = csrUtil.getPublicKeyByCsr(csr);
        //参数： 签发者信息，证书序列号，证书生效时间，证书过期时间，证书主体信息，签发公钥
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                authCa,
                serialNumber,
                startDate,
                endDate,
                csr.getSubject(),
                publicKey);
        // 构建 PolicyQualifierInfo 对象
        PolicyQualifierInfo policyQualifierInfo = new PolicyQualifierInfo("This certificate may be used for demonstration purposes only.");
        // 将 PolicyQualifierInfo 转换为 ASN1Sequence
        ASN1EncodableVector policyQualifierInfoVector = new ASN1EncodableVector();
        policyQualifierInfoVector.add(policyQualifierInfo);
        ASN1Sequence policyQualifierInfoSequence = new DERSequence(policyQualifierInfoVector);
        // 构建 PolicyInformation 对象
        PolicyInformation policyInformation = new PolicyInformation(new ASN1ObjectIdentifier("1.3.6.1.4.1.2706.2.2.1.1.1.1.1"), policyQualifierInfoSequence);

        CertificatePolicies certificatePolicies = new CertificatePolicies(new PolicyInformation[]{policyInformation});
        builder.addExtension(Extension.certificatePolicies, false, certificatePolicies);

        builder.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));
        builder.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.dataEncipherment | KeyUsage.keyEncipherment | KeyUsage.digitalSignature | KeyUsage.cRLSign));


        // 根据签发者私钥生成签名
        ContentSigner signer = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM).setProvider("BC").build(privateKey);
        X509CertificateHolder holder = builder.build(signer);

        Certificate certificate = holder.toASN1Structure();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificate.getEncoded()));
        return cert;
    }

    /**
     * 签发证书
     *
     * @param csr        证书请求文件字符串
     * @param privateKey
     * @return
     * @throws Exception
     */
    public X509Certificate generateCertByCsr(String csr, PrivateKey privateKey) throws Exception {
        PKCS10CertificationRequest csrObject = csrUtil.getCsrObject(csr);
        return generateCertByCsr(csrObject, privateKey);
    }

    /**
     * 将证书字符串转为证书对象
     *
     * @param certStr 证书字符串
     * @return
     * @throws CertificateException
     */
    public X509Certificate getCertByStr(String certStr) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64Decoder.decode(certStr)));
            return cert;
        } catch (CertificateException e) {
            throw new CustomException(NetCodeEnum.CERT_ERROR);
        }
    }

    /**
     * 验证证书合法性
     *
     * @param cert      证书对象
     * @param publicKey 签发者公钥
     * @return
     */
    public void verifyCert(X509Certificate cert, PublicKey publicKey) {
        // 验证证书的合法性
        try {
            cert.checkValidity();
            cert.verify(publicKey);
        } catch (Exception e) {
            throw new CustomException(NetCodeEnum.CERT_ERROR);
        }
    }

    /**
     * 验证证书合法性
     *
     * @param cert      证书对象
     * @param publicKey 签发者公钥
     * @return
     */
    public void verifyCert(String cert, PublicKey publicKey) {
        try {
            X509Certificate certObject = this.getCertByStr(cert);
            // 验证证书的合法性
            verifyCert(certObject, publicKey);
        } catch (Exception e) {
            throw new CustomException(NetCodeEnum.CERT_ERROR);
        }
    }

    /**
     * 打印证书
     *
     * @param cert 证书字符串
     */
    public static void lf(String cert) {
        assert cert != null;
        int lineLength = 64;
        StringBuilder sb = new StringBuilder();
        char[] chars = cert.toCharArray();
        int n = 0;
        for (char aChar : chars) {
            sb.append(aChar);
            n++;
            if (n == lineLength) {
                n = 0;
                sb.append("\n");
            }
        }
        if (n != 0)
            sb.append("\n");
        String certFileContent =
                "-----BEGIN CERTIFICATE-----\n" +
                        sb.toString() +
                        "-----END CERTIFICATE-----";
        System.out.println(certFileContent);
    }

    public static void main(String[] args) {
        String cert = "MIIEMDCCAxigAwIBAgIGAYz9MkQJMA0GCSqGSIb3DQEBCwUAMGYxITAfBgkqhkiG9w0BCQEMElRlc3RAdHJhbnNzaW9uLmNvbTELMAkGA1UEBhMCSEsxDTALBgNVBAoTBFRlc3QxCzAJBgNVBAsTAjAwMQswCQYDVQQLEwIxNTELMAkGA1UEAxMCTk4wHhcNMjEwMTEyMTAyMjU5WhcNMzQwMTEyMTAyMjU5WjB/MRcwFQYDVQQDEw5LTVMgRmFjdG9yeSBDQTELMAkGA1UEBhMCQ04xETAPBgNVBAgTCFNoYW5nSGFpMREwDwYDVQQHEwhTaGFuZ0hhaTEdMBsGA1UEChMUa21zLnRyYW5zc2lvbi1vcy5jb20xEjAQBgNVBAsTCXRyYW5zc2lvbjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJFM3v3LsHEYDVtj072+u38f0/qlQL1MTfAyjDdAwzcanAFhBSx5wakHUkj3jzMj+SyikesrM6T4XkA8r+XdBZvUgn411YUq/b+jDcJbXdF1WRCOez5FfbRRwFNZuucO450cRwNibbOpxnYUc5vxOxZuMnBOLiayAMTci54wJ/8J2qWVJCbiKrmueLhcYetAoY/uIW9CSFal4AO9ZYtOSyG6zKt4V0zm5QL2tujn6j5n3pq5bKsXGzxRB9XwSiaOnA/BluxsEdkmedN5oE64xeGIyyZsjWydn7NBHU2qvfF+0SJwwaw5FjkPu5PHJ+wYQF+DqLMjHhoTeAuq6EK6t40CAwEAAaOByjCBxzAfBgNVHSMEGDAWgBR9YTKR5P3jDUYyoxg8+cy81NfqyjBqBgNVHSAEYzBhMF8GDisGAQQBlRICAgEBAQEBME0wSwYIKwYBBQUHAgIwPww9VGhpcyBjZXJ0aWZpY2F0ZSBtYXkgYmUgdXNlZCBmb3IgZGVtb25zdHJhdGlvbiBwdXJwb3NlcyBvbmx5LjAMBgNVHRMEBTADAQH/MAsGA1UdDwQEAwIBtjAdBgNVHQ4EFgQUQYzjdOFf7K4ZATqkIZdYfcL/ZWQwDQYJKoZIhvcNAQELBQADggEBAHQWGmwPqZZQB900a6QuKXW5R3L0DaVRrKgEXXncP0CoQEqNTpNHXEWEEAxJHFr+tjEZU/4kcKoAEm9MqGdKzXz3vreiLp7WrMBSDG8GAEAu5U5dgbveFU0AeW5nPgOkhlvjZKUDfCtoHvANLgA6XLvRW4aH5rrQHNqb33VlwGatKCkq6++Uu4sdhr5EaZ5qAP2KRPeGZn+A+2gplWZGINqUIVoT1ptLolkZ7M2bPDRcSd4rUTkIT8yUI1+LN7L3cGIBwp+iLlRlOxuu3Tj4VHqkh6YW4Wj8+oi5JmTxY64H8Y0REZHLVbUo0sm6wZrGgfN8AvliWqsKHh6sbSUV/DM=";
        lf(cert);
    }

}

