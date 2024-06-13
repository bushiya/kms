package com.transsion.authenticationfactory.infrastructure.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.transsion.authenticationfactory.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationfactory.infrastructure.exception.CustomException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Date;

/**
 * @Description: 证书工具类
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
public class CertUtil {
    //签名算法
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    //生效时间
    private static final long START_DATE = 60 * 60 * 1000l;
    //有效时间 100 年
    private static final long END_DATE = 100L * 365 * 24 * 60 * 60 * 1000;

    // 名称
    public static String CN = "KMS Factory CA";
    // 国家
    public static String C = "CN";
    // 省份
    public static String ST = "ShangHai";
    // 城市
    public static String L = "ShangHai";
    // 组织
    public static String O = "kms.transsion-os.com";
    // 组织单位
    public static String OU = "transsion1";
    // 出生地
    public static String PLACE_OF_BIRTH = "出生地";

    public static X500Name factoryCA = null;

    static String key1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu2v7n9pF4TyMnACLidpXxxk/ZgqK24NYvsPsBTYKsgYUHtmfsgXtCsNTeCaNplOiv+bKtFp8Q3cs5rP4GP3OTMcmUj9sKVv6re6jNQlAZSpSLXIWylf6YmUdkqE+yVxVlDjGrMjeoPPMfzqGevoX12JKawK4EGSHBhHu80T8FHRTr4o8hqchKPVUH6aAUc99YrIdQBgUuYjAegds1p661cYF0JijhMsNvBvObczX+2HZt+HBRf2TlpC+5M4RrfFjFLxrercHk7GtyjBUHjILcYsVMgyKus+D+BinIy49Cr9+RL4ojDH2PwpTlmjaAMQif4zWfEqxlQdKhWmpDUeHSwIDAQAB";
    static String key2 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7a/uf2kXhPIycAIuJ2lfHGT9mCorbg1i+w+wFNgqyBhQe2Z+yBe0Kw1N4Jo2mU6K/5sq0WnxDdyzms/gY/c5MxyZSP2wpW/qt7qM1CUBlKlItchbKV/piZR2SoT7JXFWUOMasyN6g88x/OoZ6+hfXYkprArgQZIcGEe7zRPwUdFOvijyGpyEo9VQfpoBRz31ish1AGBS5iMB6B2zWnrrVxgXQmKOEyw28G85tzNf7Ydm34cFF/ZOWkL7kzhGt8WMUvGt6tweTsa3KMFQeMgtxixUyDIq6z4P4GKcjLj0Kv35EviiMMfY/ClOWaNoAxCJ/jNZ8SrGVB0qFaakNR4dLAgMBAAECggEAWuPFBQJvPQmGDulcwh92zgD6c9vM9yM9WFXfEa/hg+/athNHlKe3Cq8qvWvE6dJJbNLZnpj7BiS3S7IfMM90ylsRxHUpWO1YJh1CXKf96JGbhJp9XudHb4wlyUQVFMWIYvcdshE/lcJle5XAPccm5Lav7DFHXfvCVzk/Lg5YSPpEJTaSnxx6FHiBih13n1bJ8U02pqOSYHt3ZyJrJTb8MleUs2C5+MIvGIoDZwYYPWp0vv4bYry+kqxQjBLYxDwk/KfR4wZOjkBwRnU64ydChWmH1/I9DcUPfDUmDHmQYpa2TO449ETBo51tRU+oTRY8Uzg8IaNKQyhUV/Cv29pLsQKBgQDlac/bxf4LVhcHtBYe5e0LwOMhmdTT6v6Fq3LQBkHQ0ciI46UbsnFxiEZWHfU/oz+o9h8Tp10Eh37uKN1om0WsaInvRzTff+DeoOoIUvCEmyz705AZVkWRsJ8o1rxGj5KAJNqwI6TmxQQxiQysHa5LaLs4DekfmM5dtU9uNIg7rQKBgQDRJGAFCtZHlD67O+96f3A/ZrFLChvv61hvdwJl8nM7GfMWu9ZYTVSiKEIVFWZp999JDgiCywpr6X6YO+/ZIr2upL99SjSbjC3/929ddpkaF1Z4FR8ksbQgsoCtHdfA3d8QCp6WA6AzF6IVl+mcHrxCNQ1kKcpdboAJmU4OeeAt1wKBgHO767atrbm+VqP2P+qah/mT94Gt/2ZheMGqeqNjZLSaAJa+cT8FyKFD5GfxeRX/n0/3yjm6Bv0P9gLj7r6lBoMxwdRKkevmsWTevPIyFVBwkEMCugFuOm6DMegCXL0kFP48mW6erMQWxETRJOl3l8RHM5vVuLPN/N3daBLqwYeJAoGBAIGzQEUAQoEjg3/UHKDoVXFMO1khDNBOMIr30CnvIN0fK60GklgypPDnbIvsuJWxHe0NYePTDUR9CoBYc1dCf0XGb+bAVT29wm1CQvNAcJRIstxHpQe5mM7KRJiXsEywi/xDybOYzgnBd0hS7JJgUTnx8rsSW6hztb2+MDU0Cf4DAoGBAMgwvJH9OF4UC5b3PH1xJc8doL7LGE4iDfJLWWuZx1VpX8fxV1xOsGWGW0W34seWetiafIHS6nI4zi8thQUBm/mwAswpV+gAzyEsTqfxWC6pYVMaTODz6AfJdDHhoAOMzhCGVJU2xDKd5OtiOnBKmkgNbQEJ+i3vnsnMjYG93SAt";

    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();


        String s = CsrUtil.generateCsrString(keyPair.getPublic(), keyPair.getPrivate());
        X509Certificate x509Certificate = CertUtil.generateCertByCsr(s, RSAUtil.strByPrivateKey(key2));
        lf(Base64Encoder.encode(x509Certificate.getEncoded()));

    }


    /**
     * 签发证书
     *
     * @param csr        证书请求文件
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static X509Certificate generateCertByCsr(PKCS10CertificationRequest csr, PrivateKey privateKey) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        X500NameBuilder localX500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        localX500NameBuilder.addRDN(BCStyle.CN, CN);
        localX500NameBuilder.addRDN(BCStyle.C, C);
        localX500NameBuilder.addRDN(BCStyle.ST, ST);
        localX500NameBuilder.addRDN(BCStyle.L, L);
        localX500NameBuilder.addRDN(BCStyle.O, O);
        localX500NameBuilder.addRDN(BCStyle.OU, OU);
        factoryCA = localX500NameBuilder.build();
        // 证书生效时间
        Date startDate = new Date(System.currentTimeMillis() - START_DATE);
        // 证书过期时间
        Date endDate = new Date(System.currentTimeMillis() + END_DATE);
        // 证书序列号
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
        RSAPublicKey publicKey = CsrUtil.getPublicKeyByCsr(csr);
        //参数： 签发者信息，证书序列号，证书生效时间，证书过期时间，证书主体信息，签发公钥
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                factoryCA,
                serialNumber,
                startDate,
                endDate,
                csr.getSubject(),
                publicKey);
        builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.dataEncipherment | KeyUsage.keyEncipherment | KeyUsage.digitalSignature | KeyUsage.cRLSign));
        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
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
    public static X509Certificate generateCertByCsr(String csr, PrivateKey privateKey) throws Exception {
        PKCS10CertificationRequest csrObject = CsrUtil.getCsrObject(csr);
        return generateCertByCsr(csrObject, privateKey);
    }

    /**
     * 将证书字符串转为证书对象
     *
     * @param certStr 证书字符串
     * @return
     * @throws CertificateException
     */
    public static X509Certificate getCertByStr(String certStr) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64Decoder.decode(certStr)));
        return cert;
    }

    /**
     * 验证证书合法性
     *
     * @param cert      证书对象
     * @param publicKey 签发者公钥
     * @return
     */
    public static void verifyCert(X509Certificate cert, PublicKey publicKey) {
        // 验证证书的合法性
        try {
            cert.checkValidity();
            cert.verify(publicKey);
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
}

