/*Top Secret */
package com.transsion.daconsole.infrastructure.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.transsion.daconsole.infrastructure.constants.NetCodeEnum;
import com.transsion.daconsole.infrastructure.exception.CustomException;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * rsa帮助类，推荐使用分段加解密<br/>
 * <br/>
 * Copyright© 2020 Transsion Inc <br/>
 * Author xulin.tan <br/>
 * Created on 2020年6月1日 <br/>
 */
@SuppressWarnings("deprecation")
public class RsaHelper {

    private final static Logger log = LoggerFactory.getLogger(RsaHelper.class);
    /**
     * 加密解密超过阈值时打印耗时开关：true-查过阈值时打印，false-都打印
     */
    private static final boolean SWITCH_COST_TIME_THRESHOLD = false;

    /**
     * 算法名
     */
    public static final String ALGORITHM = "RSA";

    /**
     * RSA签名算法
     */
    private static final String RSA_SIGN_ALGORITHMS = "SHA256WithRSA";

    /**
     * 耗时阈值
     */
    private static final long COST_TIME_THRESHOLD = 50L;

    /**
     * 默认字符集
     */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * KEY工厂
     */
    private static KeyFactory RSA_KEY_FACTORY = null;

    static {
        try {
            RSA_KEY_FACTORY = KeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.info(e.getMessage(), e);
        }
    }

    /**
     * base64格式化公钥
     *
     * @param publicKey rsaKey
     * @return
     * @throws Exception
     */
    public static String base64PublicKey(RSAPublicKey publicKey) throws Exception {
        return Base64Encoder.encode(publicKey.getEncoded());
    }

    /**
     * base64格式化公钥
     *
     * @param rsaKey
     * @return
     * @throws Exception
     */
    public static String base64PublicKey(RsaKeyInfo rsaKey) throws Exception {
        return base64PublicKey(rsaKey.getPublicKey());
    }

    /**
     * base64格式化私钥
     *
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String base64PrivateKey(PrivateKey privateKey) throws Exception {
        return Base64Encoder.encode(privateKey.getEncoded());
    }

    /**
     * base64格式化私钥
     *
     * @param rsaKey
     * @return
     * @throws Exception
     */
    public static String base64PrivateKey(RsaKeyInfo rsaKey) throws Exception {
        return base64PrivateKey(rsaKey.getPrivateKey());
    }

    /**
     * 将base64的PKCS8的私钥转换为PKCS1的私钥
     *
     * @param pkcs8PrivateKey
     * @return
     * @throws Exception
     */
    @Deprecated
    public static String formatPrivateKeyPkcs8ToPkcs1(String pkcs8PrivateKey) throws Exception {
        // 将BASE64编码的私钥字符串进行解码
        byte[] encodeByte = Base64Decoder.decode(pkcs8PrivateKey);
        PrivateKeyInfo pki = PrivateKeyInfo.getInstance(encodeByte);
        RSAPrivateKeyStructure pkcs1Key = RSAPrivateKeyStructure.getInstance(pki.getPrivateKeyAlgorithm());
        byte[] pkcs1Bytes = pkcs1Key.getEncoded();// etc.
        return Base64Encoder.encode(pkcs1Bytes);
    }

    /**
     * 将base64的PKCS8的私钥转换为PKCS1的私钥
     *
     * @param pkcs8PublicKey
     * @return
     * @throws Exception
     */
    public static String formatPublicKeyPkcs8ToPkcs1(String pkcs8PublicKey) throws Exception {
        SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(Base64Decoder.decode(pkcs8PublicKey));
        ASN1Primitive primitive = spkInfo.parsePublicKey();
        byte[] publicKeyPKCS1 = primitive.getEncoded();
        return Base64Encoder.encode(publicKeyPKCS1);
    }


    /**
     * RSA解密
     *
     * @param content          待解密内容
     * @param rsaPrivateKeyStr rsa私钥串
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String rsaPrivateKeyStr) throws Exception {
        return decrypt(content, generatePrivateRSAKey(rsaPrivateKeyStr));
    }

    /**
     * rsa解密
     *
     * @param content          待解密内容
     * @param rsaPrivateKeyStr rsaPrivateKey rsa私钥串
     * @return
     * @throws Exception
     */
    public static String decryptBase64(String content, String rsaPrivateKeyStr) throws Exception {
        return decryptBase64(content, generatePrivateRSAKey(rsaPrivateKeyStr));
    }

    /**
     * rsa PKCS1解密
     *
     * @param content          待解密内容
     * @param rsaPrivateKeyStr rsa私钥串
     * @return
     * @throws Exception
     */
    public static String decryptPKCS1(String content, String rsaPrivateKeyStr) throws Exception {
        return decrypt(content, generatePrivateRSAKeyPKCS1(rsaPrivateKeyStr));
    }

    /**
     * rsa PKCS1解密
     *
     * @param content          待解密内容
     * @param rsaPrivateKeyStr rsa私钥串
     * @return
     * @throws Exception
     */
    public static String decryptBase64PKCS1(String content, String rsaPrivateKeyStr) throws Exception {
        return decryptBase64(content, generatePrivateRSAKeyPKCS1(rsaPrivateKeyStr));
    }

    /**
     * rsa解密
     *
     * @param content       待解密内容
     * @param rsaPrivateKey rsa私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, RSAPrivateKey rsaPrivateKey) throws Exception {
        long startTime = System.currentTimeMillis();
        long costTime = 0L;
        try {
            byte[] result = decryptByte(Base64Decoder.decode(content), rsaPrivateKey);
            return new String(result, DEFAULT_CHARSET);
        } finally {
            costTime = System.currentTimeMillis() - startTime;
            if (!SWITCH_COST_TIME_THRESHOLD) {
            } else if (costTime >= COST_TIME_THRESHOLD) {
                // 如果解密耗时超过阈值，打印日志
            }
        }
    }

    /**
     * rsa解密
     *
     * @param content       待解密内容
     * @param rsaPrivateKey rsa私钥
     * @return
     * @throws Exception
     */
    public static String decryptBase64(String content, RSAPrivateKey rsaPrivateKey) throws Exception {
        long startTime = System.currentTimeMillis();
        long costTime = 0L;
        try {
            return Base64Encoder.encode(decryptByte(Base64Decoder.decode(content), rsaPrivateKey));
        } finally {
            costTime = System.currentTimeMillis() - startTime;
            if (!SWITCH_COST_TIME_THRESHOLD) {
                log.info("rsa decrypt data finished, costTime[{}]ms, content:{}", costTime, content);
            } else if (costTime >= COST_TIME_THRESHOLD) {
                // 如果解密耗时超过阈值，打印日志
                log.info("rsa decrypt data finished, costTime[{}]ms, content:{}", costTime, content);
            }
        }
    }

    /**
     * rsa解密
     *
     * @param content       待解密内容
     * @param rsaPrivateKey rsa私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByte(byte[] content, RSAPrivateKey rsaPrivateKey) throws Exception {
        try {
            // RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * RSA分段解密
     *
     * @param content             待解密内容
     * @param rsaPrivateKeyString rsa私钥串
     * @return
     * @throws Exception
     */
    public static String decryptWithBlock(String content, String rsaPrivateKeyString) throws Exception {
        return decryptWithBlock(content, generatePrivateRSAKey(rsaPrivateKeyString));
    }

    /**
     * RSA分段解密
     *
     * @param content             待解密内容
     * @param rsaPrivateKeyString rsa私钥串
     * @return
     * @throws Exception
     */
    public static String decryptWithBlockBase64(String content, String rsaPrivateKeyString) throws Exception {
        return decryptWithBlockBase64(content, generatePrivateRSAKey(rsaPrivateKeyString));
    }

    /**
     * RSA PKCS1 分段解密
     *
     * @param content             待解密内容
     * @param rsaPrivateKeyString rsa私钥串
     * @return
     * @throws Exception
     */
    public static String decryptWithBlockPKCS1(String content, String rsaPrivateKeyString) throws Exception {
        return decryptWithBlock(content, generatePrivateRSAKeyPKCS1(rsaPrivateKeyString));
    }

    /**
     * RSA PKCS1 分段解密
     *
     * @param content             待解密内容
     * @param rsaPrivateKeyString rsa私钥串
     * @return
     * @throws Exception
     */
    public static String decryptWithBlockPKCS1Base64(String content, String rsaPrivateKeyString) throws Exception {
        return decryptWithBlockBase64(content, generatePrivateRSAKeyPKCS1(rsaPrivateKeyString));
    }

    /**
     * RSA分段解密
     *
     * @param content       待解密内容
     * @param rsaPrivateKey rsa私钥
     * @return
     * @throws Exception
     */
    public static String decryptWithBlock(String content, RSAPrivateKey rsaPrivateKey) throws Exception {

        long startTime = System.currentTimeMillis();
        long costTime = 0L;
        try {
            return new String(decryptWithBlockByte(Base64Decoder.decode(content), rsaPrivateKey), DEFAULT_CHARSET);
        } finally {
            costTime = System.currentTimeMillis() - startTime;
            if (!SWITCH_COST_TIME_THRESHOLD) {
                log.info("rsa decrypt data finished, costTime[{}]ms, content:{}", costTime, content);
            } else if (costTime >= COST_TIME_THRESHOLD) {
                // 如果解密耗时超过阈值，打印日志
                log.info("rsa decrypt data finished, costTime[{}]ms, content:{}", costTime, content);
            }
        }
    }

    /**
     * RSA分段解密
     *
     * @param content       待解密内容
     * @param rsaPrivateKey rsa私钥
     * @return
     * @throws Exception
     */
    public static String decryptWithBlockBase64(String content, RSAPrivateKey rsaPrivateKey) throws Exception {
        long startTime = System.currentTimeMillis();
        long costTime = 0L;
        try {
            return Base64Encoder.encode(decryptWithBlockByte(Base64Decoder.decode(content), rsaPrivateKey));
        } finally {
            costTime = System.currentTimeMillis() - startTime;
            if (!SWITCH_COST_TIME_THRESHOLD) {
                log.info("rsa decrypt data finished, costTime[{}]ms, content:{}", costTime, content);
            } else if (costTime >= COST_TIME_THRESHOLD) {
                // 如果解密耗时超过阈值，打印日志
                log.info("rsa decrypt data finished, costTime[{}]ms, content:{}", costTime, content);
            }
        }
    }

    /**
     * RSA分段解密
     *
     * @param content       待解密内容
     * @param rsaPrivateKey rsa私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptWithBlockByte(byte[] content, RSAPrivateKey rsaPrivateKey) throws Exception {
        byte[] plainText = null;
        try {
            int length = content.length;
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            byte[] tmp = null;
            int offset = 0;
            int blockSize = getMaxBlockSize(rsaPrivateKey);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (length - offset > 0) {
                if (length - offset > blockSize) {
                    tmp = cipher.doFinal(content, offset, blockSize);
                } else {
                    tmp = cipher.doFinal(content, offset, length - offset);
                }
                baos.write(tmp, 0, tmp.length);
                offset += blockSize;
            }
            plainText = baos.toByteArray();
            return plainText;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * RSA加密
     *
     * @param content
     * @param rsaPublicKey
     * @return
     * @throws Exception
     */
    public static String encryptBase64(String content, String rsaPublicKey) throws Exception {
        return encryptBase64(content, generatePublicRSAKey(rsaPublicKey));
    }

    /**
     * RSA PKCS1 加密
     *
     * @param content
     * @param publicKeyString
     * @return
     * @throws Exception
     */
    public static String encryptBase64PKCS1(String content, String publicKeyString) throws Exception {
        return encryptBase64(content, generatePublicRSAKeyPKCS1(publicKeyString));
    }

    /**
     * RSA PKCS1 加密
     *
     * @param content
     * @param publicKeyString
     * @return
     * @throws Exception
     */
    public static String encryptBase64PKCS1(byte[] content, String publicKeyString) throws Exception {
        return encryptBase64(content, generatePublicRSAKeyPKCS1(publicKeyString));
    }

    /**
     * RSA加密
     *
     * @param content
     * @param rsaPublicKey publicKeyString
     * @return
     * @throws Exception
     */
    public static String encryptBase64(String content, RSAPublicKey rsaPublicKey) throws Exception {
        return encryptBase64(content.getBytes(DEFAULT_CHARSET), rsaPublicKey);
    }

    /**
     * RSA加密
     *
     * @param content
     * @param rsaPublicKey
     * @return
     * @throws Exception
     */
    public static String encryptBase64(byte[] content, RSAPublicKey rsaPublicKey) throws Exception {
        return Base64Encoder.encode(encrypt(content, rsaPublicKey));
    }

    /**
     * RSA加密
     *
     * @param content
     * @param rsaPublicKey publicKeyString
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String content, RSAPublicKey rsaPublicKey) throws Exception {
        return encrypt(content.getBytes(DEFAULT_CHARSET), rsaPublicKey);
    }

    /**
     * RSA加密
     *
     * @param content
     * @param rsaPublicKey publicKeyString
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] content, RSAPublicKey rsaPublicKey) throws Exception {

        long startTime = System.currentTimeMillis();
        long costTime = 0L;
        try {
            // RSA加密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        } finally {
            costTime = System.currentTimeMillis() - startTime;
            if (!SWITCH_COST_TIME_THRESHOLD) {
                log.info("encrypt data finished, costTime[{}]ms", costTime);
            } else if (costTime >= COST_TIME_THRESHOLD) {
                // 如果加密耗时超过阈值，打印日志
                log.info("encrypt data finished, costTime[{}]ms", costTime);
            }
        }
    }

    /**
     * 分段RSA加密
     *
     * @param content            待加密内容
     * @param rsaPublicKeyString rsa公钥字符串
     * @return
     * @throws Exception
     */
    public static String encryptWithBlockBase64(String content, String rsaPublicKeyString) throws Exception {
        return encryptWithBlockBase64(content, generatePublicRSAKey(rsaPublicKeyString));
    }

    /**
     * 分段RSA加密
     *
     * @param content            待加密内容
     * @param rsaPublicKeyString rsa公钥字符串
     * @return
     * @throws Exception
     */
    public static String encryptWithBlockBase64(byte[] content, String rsaPublicKeyString) throws Exception {
        return encryptWithBlockBase64(content, generatePublicRSAKey(rsaPublicKeyString));
    }

    /**
     * 分段RSA加密
     *
     * @param content            待加密内容
     * @param rsaPublicKeyString rsa公钥字符串
     * @return
     * @throws Exception
     */
    public static String encryptWithBlockBase64PKCS1(String content, String rsaPublicKeyString) throws Exception {
        return encryptWithBlockBase64(content, generatePublicRSAKeyPKCS1(rsaPublicKeyString));
    }

    /**
     * 分段RSA加密
     *
     * @param content            待加密内容
     * @param rsaPublicKeyString rsa公钥字符串
     * @return
     * @throws Exception
     */
    public static String encryptWithBlockBase64PKCS1(byte[] content, String rsaPublicKeyString) throws Exception {
        return encryptWithBlockBase64(content, generatePublicRSAKeyPKCS1(rsaPublicKeyString));
    }

    /**
     * 分段RSA加密
     *
     * @param content   待加密内容
     * @param publicKey rsa公钥字符
     * @return
     * @throws Exception
     */
    public static String encryptWithBlockBase64(String content, RSAPublicKey publicKey) throws Exception {
        return Base64Encoder.encode(encryptWithBlock(content.getBytes(DEFAULT_CHARSET), publicKey));
    }

    /**
     * 分段RSA加密
     *
     * @param content   待加密内容
     * @param publicKey rsa公钥字符
     * @return
     * @throws Exception
     */
    public static String encryptWithBlockBase64(byte[] content, RSAPublicKey publicKey) throws Exception {
        return Base64Encoder.encode(encryptWithBlock(content, publicKey));
    }

    /**
     * 分段RSA加密
     *
     * @param content   待加密内容
     * @param publicKey rsa公钥字符
     * @return
     * @throws Exception
     */
    public static byte[] encryptWithBlock(String content, RSAPublicKey publicKey) throws Exception {
        return encryptWithBlock(content.getBytes(DEFAULT_CHARSET), publicKey);
    }

    /**
     * 分段RSA加密
     *
     * @param content   待加密内容
     * @param publicKey rsa公钥字符
     * @return
     * @throws Exception
     */
    public static byte[] encryptWithBlock(byte[] content, RSAPublicKey publicKey) throws Exception {

        long startTime = System.currentTimeMillis();
        long costTime = 0L;
        try {
            int blockSize = getMaxBlockSize(publicKey) - 11;
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] tmp = null;
            int offset = 0;
            int length = content.length;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (length - offset > 0) {
                if (length - offset > blockSize) {
                    tmp = cipher.doFinal(content, offset, blockSize);
                } else {
                    tmp = cipher.doFinal(content, offset, length - offset);
                }
                baos.write(tmp, 0, tmp.length);
                offset += blockSize;
            }
            byte[] ciphered = baos.toByteArray();
            return ciphered;

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        } finally {
            costTime = System.currentTimeMillis() - startTime;
            if (!SWITCH_COST_TIME_THRESHOLD) {
                log.info("encrypt data finished, costTime[{}]ms", costTime);
            } else if (costTime >= COST_TIME_THRESHOLD) {
                // 如果加密耗时超过阈值，打印日志
                log.info("encrypt data finished, costTime[{}]ms", costTime);
            }
        }
    }

    /**
     * 生成RSA密钥
     *
     * @param lenth 密钥长度
     * @return
     * @throws Exception
     */
    public static RsaKeyInfo generateRSAKey(RsaLengthEnum lenth) throws Exception {

        if (lenth == null) {
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }

        try {

            // 获得对象 KeyPairGenerator 参数 RSA 1024个字节
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGen.initialize(lenth.length);
            // 通过对象 KeyPairGenerator 获取对象KeyPair
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // 通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            return new RsaKeyInfo(publicKey, privateKey);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 将字符串格式公钥转换为RSAPrivateKey
     *
     * @param privateKeyStr
     * @return
     */
    public static RSAPrivateKey generatePrivateRSAKey(String privateKeyStr) {
        try {
            byte[] pribyte = Base64Decoder.decode(privateKeyStr.getBytes(DEFAULT_CHARSET));
            PKCS8EncodedKeySpec encodedKey = new PKCS8EncodedKeySpec(pribyte);
            return (RSAPrivateKey) RSA_KEY_FACTORY.generatePrivate(encodedKey);
        } catch (Exception e) {
            log.info(String.format("generatePrivateRSAKey, privateKeyStr:%s", privateKeyStr), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 将字符串格式公钥转换为RSAPublicKey
     *
     * @param publicKeyStr
     * @return
     */
    public static RSAPublicKey generatePublicRSAKey(String publicKeyStr) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                Base64Decoder.decode(publicKeyStr.getBytes(DEFAULT_CHARSET)));

        try {
            return (RSAPublicKey) RSA_KEY_FACTORY.generatePublic(keySpec);
        } catch (Exception e) {
            log.info(String.format("generatePublicRSAKey, publicKeyStr:%s", publicKeyStr), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }

    }

    /**
     * 将字符串格式公钥转换为PKCS1的RSAPublicKey
     *
     * @param publicKeyStr
     * @return
     */
    public static RSAPublicKey generatePublicRSAKeyPKCS1(String publicKeyStr) {

        try {
            org.bouncycastle.asn1.pkcs.RSAPublicKey rsaPublicKey = org.bouncycastle.asn1.pkcs.RSAPublicKey
                    .getInstance(Base64Decoder.decode(publicKeyStr.getBytes(DEFAULT_CHARSET)));
            java.security.spec.RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(
                    rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent());
            return (RSAPublicKey) RSA_KEY_FACTORY.generatePublic(publicKeySpec);
        } catch (Exception e) {
            log.info(String.format("generatePublicRSAKey, publicKeyStr:%s", publicKeyStr), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }

    }

    /**
     * 将字符串格式公钥转换为PKCS1的RSAPrivateKey
     *
     * @param privateKeyStr
     * @return
     */
    public static RSAPrivateKey generatePrivateRSAKeyPKCS1(String privateKeyStr) {

        try {
            org.bouncycastle.asn1.pkcs.RSAPrivateKey privateKey = org.bouncycastle.asn1.pkcs.RSAPrivateKey
                    .getInstance(Base64Decoder.decode(privateKeyStr.getBytes(DEFAULT_CHARSET)));
            java.security.spec.RSAPrivateKeySpec privateKeySpec = new java.security.spec.RSAPrivateKeySpec(
                    privateKey.getModulus(), privateKey.getPrivateExponent());
            return (RSAPrivateKey) RSA_KEY_FACTORY.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            log.info(String.format("generatePrivateRSAKeyPKCS1, privateKeyStr:%s", privateKeyStr), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }

    }

    /**
     * RSA私钥签名
     *
     * @param content       待签名内容
     * @param rsaPrivateKey 签名私钥
     * @return
     */
    public static String signBase64(String content, String rsaPrivateKey) {
        return signBase64(content.getBytes(DEFAULT_CHARSET), rsaPrivateKey);
    }

    /**
     * RSA私钥签名
     *
     * @param content             待签名内容
     * @param rsaPrivateKeyString 签名私钥字符串格式
     * @return
     */
    public static String signBase64PKCS1(String content, String rsaPrivateKeyString) {
        return signBase64(content.getBytes(DEFAULT_CHARSET), generatePrivateRSAKeyPKCS1(rsaPrivateKeyString));
    }

    /**
     * RSA私钥签名
     *
     * @param content             待签名内容
     * @param rsaPrivateKeyString 签名私钥字符串格式
     * @return
     */
    public static String signBase64(byte[] content, String rsaPrivateKeyString) {
        return signBase64(content, generatePrivateRSAKey(rsaPrivateKeyString));
    }

    /**
     * RSA私钥签名
     *
     * @param content             待签名内容
     * @param rsaPrivateKeyString 签名私钥字符串格式
     * @return
     */
    public static String signBase64PKCS1(byte[] content, String rsaPrivateKeyString) {
        return signBase64(content, generatePrivateRSAKeyPKCS1(rsaPrivateKeyString));
    }

    /**
     * RSA私钥签名
     *
     * @param content       待签名内容
     * @param rsaPrivateKey 签名私钥
     * @return
     */
    public static String signBase64(byte[] content, RSAPrivateKey rsaPrivateKey) {
        long startTime = System.currentTimeMillis();
        long costTime = 0L;
        try {
            return Base64Encoder.encode(sign(content, rsaPrivateKey));
        } finally {
            costTime = System.currentTimeMillis() - startTime;
            if (!SWITCH_COST_TIME_THRESHOLD) {
                log.info("rsa sign data finished, costTime[{}]ms, content:{}", costTime, content);
            } else if (costTime >= COST_TIME_THRESHOLD) {
                // 如果加密耗时超过阈值，打印日志
                log.info("rsa sign data finished, costTime[{}]ms, content:{}", costTime, content);
            }
        }
    }

    /**
     * RSA私钥签名
     *
     * @param content       待签名内容
     * @param rsaPrivateKey 签名私钥
     * @return
     */
    public static byte[] sign(byte[] content, RSAPrivateKey rsaPrivateKey) {
        if (content == null) {
            return null;
        }

        try {
            Signature signature = Signature.getInstance(RSA_SIGN_ALGORITHMS);
            signature.initSign(rsaPrivateKey);
            signature.update(content);
            return signature.sign();
        } catch (Exception e) {
            log.info(String.format("sign data failure, content:%s", content), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * rsa验证签名
     *
     * @param signValue          签名值
     * @param content            内容
     * @param rsaPublicKeyString rsa公钥字符串
     * @return
     */
    public static Boolean verify(String signValue, String content, String rsaPublicKeyString) {
        return verify(Base64Decoder.decode(signValue), content.getBytes(DEFAULT_CHARSET),
                generatePublicRSAKey(rsaPublicKeyString));
    }

    /**
     * rsa验证签名
     *
     * @param signValue          签名值
     * @param content            内容
     * @param rsaPublicKeyString rsa公钥字符串
     * @return
     */
    public static Boolean verifyPKCS1(String signValue, String content, String rsaPublicKeyString) {
        return verify(Base64Decoder.decode(signValue), content.getBytes(DEFAULT_CHARSET),
                generatePublicRSAKeyPKCS1(rsaPublicKeyString));
    }

    /**
     * rsa验证签名
     *
     * @param signValue    签名值
     * @param content      内容
     * @param rsaPublicKey rsa公钥
     * @return
     */
    public static Boolean verify(byte[] signValue, byte[] content, String rsaPublicKey) {
        return verify(signValue, content, generatePublicRSAKey(rsaPublicKey));
    }

    /**
     * rsa验证签名
     *
     * @param signValue          签名值
     * @param content            内容
     * @param rsaPublicKeyString rsa公钥字符串
     * @return
     */
    public static Boolean verifyPKCS1(byte[] signValue, byte[] content, String rsaPublicKeyString) {
        return verify(signValue, content, generatePublicRSAKeyPKCS1(rsaPublicKeyString));
    }

    /**
     * rsa验证签名
     *
     * @param signValue    签名值
     * @param content      内容
     * @param rsaPublicKey rsa公钥
     * @return
     */
    public static Boolean verify(byte[] signValue, byte[] content, RSAPublicKey rsaPublicKey) {
        if (content == null) {
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
        if (signValue == null) {
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
        if (rsaPublicKey == null) {
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }

        long startTime = System.currentTimeMillis();
        long costTime = 0L;
        try {
            Signature signature = Signature.getInstance(RSA_SIGN_ALGORITHMS);
            signature.initVerify(rsaPublicKey);
            signature.update(content);
            return signature.verify(signValue);
        } catch (Exception e) {
            log.info(String.format("verify sign exception, signValue：[%s], content：%s", signValue, content), e);
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        } finally {
            costTime = System.currentTimeMillis() - startTime;
            if (!SWITCH_COST_TIME_THRESHOLD) {
            } else if (costTime >= COST_TIME_THRESHOLD) {
                // 如果加密耗时超过阈值，打印日志
            }
        }
    }

    private static int getMaxBlockSize(RSAKey key) {
        return key.getModulus().bitLength() / 8;
    }

    /**
     * RSA算法长度枚举值<br/>
     * <br/>
     * Copyright© 2020 Transsion Inc <br/>
     * Author xulin.tan <br/>
     * Created on 2020年6月1日 <br/>
     */
    @Getter
    public enum RsaLengthEnum {

        /**
         * RSA长度：1024
         */
        RSA("RSA", 1024),

        /**
         * RSA2长度：2048
         */
        RSA2("RSA", 2048),

        ;

        /**
         * 算法
         */
        private String algorithm;

        /**
         * 长度
         */
        private int length;

        /**
         * 私有化构造函数
         *
         * @param algorithm
         * @param length
         */
        RsaLengthEnum(String algorithm, int length) {
            this.algorithm = algorithm;
            this.length = length;
        }

        /**
         * 解析长度
         *
         * @param length
         */
        public static RsaLengthEnum parse(int length) {
            for (RsaLengthEnum var : values()) {
                if (var.length == length) {
                    return var;
                }
            }

            return null;
        }
    }

    /**
     * rsa密钥信息<br/>
     * <br/>
     * Copyright© 2020 Transsion Inc <br/>
     * Author xulin.tan <br/>
     * Created on 2020年6月1日 <br/>
     */
    @Getter
    @Setter
    public static class RsaKeyInfo {

        /**
         * 公钥
         */
        private RSAPublicKey publicKey;

        /**
         * 私钥
         */
        private RSAPrivateKey privateKey;

        /**
         * 默认构造函数
         */
        public RsaKeyInfo() {
            super();
        }

        /**
         * 构造函数
         *
         * @param publicKey
         * @param privateKey
         */
        public RsaKeyInfo(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
            super();
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

    }

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK_245 = 245;
    private static final int MAX_ENCRYPT_BLOCK_117 = 117;

    /** */
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK_256 = 256;
    private static final int MAX_DECRYPT_BLOCK_128 = 128;

    public static byte[] encryptByPrivatePkcs1(String data, org.bouncycastle.asn1.pkcs.RSAPrivateKey rsaPrivateKey) throws Exception {
        byte[] b = data.getBytes();
        return encryptByPrivatePkcs1(b, rsaPrivateKey);
    }


    public static byte[] encryptByPrivatePkcs1(byte[] b, org.bouncycastle.asn1.pkcs.RSAPrivateKey rsaPrivateKey) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        Key privateKey = new RSAPrivateKey() {
            @Override
            public BigInteger getPrivateExponent() {
                return rsaPrivateKey.getPrivateExponent();
            }

            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }

            @Override
            public BigInteger getModulus() {
                return rsaPrivateKey.getModulus();
            }
        };
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        // 对数据分段
//		byte[] b = data.getBytes();
        int inputLen = b.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK_245) {
                cache = cipher.doFinal(b, offSet, MAX_ENCRYPT_BLOCK_245);
            } else {
                cache = cipher.doFinal(b, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK_245;
        }
        return out.toByteArray();
    }


    public static byte[] decryptByPublicPkcs1(byte[] data, org.bouncycastle.asn1.pkcs.RSAPublicKey rsaPublicKey) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
        Key publicKey = new RSAPublicKey() {
            @Override
            public BigInteger getPublicExponent() {
                return rsaPublicKey.getPublicExponent();
            }

            @Override
            public String getAlgorithm() {
                return "RSA";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }

            @Override
            public BigInteger getModulus() {
                return rsaPublicKey.getModulus();
            }
        };
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        // 对数据分段解密
        byte[] b = data;
        int inputLen = b.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK_256) {
                cache = cipher.doFinal(b, offSet, MAX_DECRYPT_BLOCK_256);
            } else {
                cache = cipher.doFinal(b, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK_256;
        }
        return out.toByteArray();
    }
    /** */
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    public static String decryptByPublicKey(String publicKey, String text, int rsaType) throws Exception {
        int MAX_DECRYPT_BLOCK = MAX_DECRYPT_BLOCK_128;
        if (rsaType == 1024) {
            MAX_DECRYPT_BLOCK = MAX_DECRYPT_BLOCK_128;
        } else {
            MAX_DECRYPT_BLOCK = MAX_DECRYPT_BLOCK_256;
        }
        byte[] encryptedData = java.util.Base64.getDecoder().decode(text);
        byte[] keyBytes = java.util.Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }


    public static void main(String[] args) throws Exception {


    }

}
