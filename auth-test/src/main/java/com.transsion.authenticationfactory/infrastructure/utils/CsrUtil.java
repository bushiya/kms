package com.transsion.authenticationfactory.infrastructure.utils;


import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
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

import java.io.IOException;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @Description: 证书请求文件工具类
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
public class CsrUtil {
    // 名称
    public static String CN = "KMS Device License CA";
    // 国家
    public static String C = "CN";
    // 省份
    public static String ST = "ShangHai";
    // 城市
    public static String L = "ShangHai";
    // 组织
    public static String O = "kms.transsion-os.com";
    // 组织单位
    public static String OU = "transsion";

    public static void main(String[] args) throws Exception {
        Map<String, String> map = RSAUtil.initRSAKey(2048);
        String s = generateCsrString(RSAUtil.strByPublicKey(map.get("publicKey")), RSAUtil.strByPrivateKey(map.get("privateKey")));
        System.out.println(s);
    }

    /**
     * 获取证书请求字符串
     *
     * @param publicKey  请求者公钥
     * @param privateKey 请求者私钥
     * @return
     * @throws Exception
     */
    public static String generateCsrString(PublicKey publicKey, PrivateKey privateKey) throws Exception {
        X500NameBuilder localX500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        localX500NameBuilder.addRDN(BCStyle.CN, CN);
        localX500NameBuilder.addRDN(BCStyle.C, C);
        localX500NameBuilder.addRDN(BCStyle.ST, ST);
        localX500NameBuilder.addRDN(BCStyle.L, L);
        localX500NameBuilder.addRDN(BCStyle.O, O);
        localX500NameBuilder.addRDN(BCStyle.OU, OU);
        // 使用私钥和 SHA256WithRSA/SM3withSM2 算法创建签名者对象
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);
        // 创建 CSR
        PKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(localX500NameBuilder.build(), publicKey);
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
    public static PKCS10CertificationRequest getCsrObject(String csrString) throws Exception {
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
    public static RSAPublicKey getPublicKeyByCsr(PKCS10CertificationRequest csr) throws Exception {
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
    public static RSAPublicKey getPublicKeyByCsr(String csr) throws Exception {
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
    public static void printOpensslPemFormatCsrFileContent(PKCS10CertificationRequest csr) throws IOException {
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
    public static void printOpensslPemFormatCsrFileContent(String csr) throws Exception {
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

