package com.transsion.authentication.infrastructure.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 售后特征码接口 AES加密工具类
 * @Author jiakang.chen
 * @Date 2023/8/17
 */
@Slf4j
@Component
public class AfterSaveUtils {
    private static final String IV = "5&rgXLuPg#Uf5YNe";
    private static final String secretKey = "slYx5GCLdbZu3Ak!s0fu30vJg#1Ki1kN";

    private static byte[] encrypt(String content, String pkey) {
        try {
            SecretKeySpec skey = new SecretKeySpec(pkey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return encrypted;
        } catch (Exception e) {
            log.info("encrypt() method error:", e);
        }
        return null;
    }

    private static String encryptStr(String content, String pkey) {
        byte[] aesEncrypt = encrypt(content, pkey);
        String base64EncodeStr = Base64Encoder.encode(aesEncrypt);
        return base64EncodeStr;
    }

    private static String decodeStr(String content, String pkey) {
        try {
            byte[] base64DecodeStr = Base64Decoder.decode(content);
            byte[] aesDecode = decode(base64DecodeStr, pkey);
            if (aesDecode == null) {
                return null;
            }
            String result = new String(aesDecode, StandardCharsets.UTF_8);
            return result;
        } catch (Exception e) {
            log.info("decodeStr() method error:", e);
        }
        return null;
    }

    private static byte[] decode(byte[] content, String pkey) throws Exception {
        SecretKeySpec skey = new SecretKeySpec(pkey.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey, iv);
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public static String decode(String content) {
        return decodeStr(content, secretKey);
    }

    public static String encrypt(String content) {
        return encryptStr(content, secretKey);
    }

}