package com.transsion.daconsole.infrastructure.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

/**
 * &#064;Description:
 * &#064;author： dong.li on 2022/10/09
 * =======================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

public class AESUtils {


    //16位密钥
    public final static String key = "authentication88";

    /**
     * 加密
     */
    public static String encrypt(String data) {
        AES aes = SecureUtil.aes(StrUtil.bytes(key, CharsetUtil.CHARSET_UTF_8));
        byte[] encrypt = aes.encrypt(data);
        return Base64.encode(encrypt);
    }

    /**
     * 解密
     */
    public static String decrypt(String data) {
        AES aes = SecureUtil.aes(StrUtil.bytes(key, CharsetUtil.CHARSET_UTF_8));
        byte[] decrypt = aes.decrypt(Base64.decode(data));
        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 加密
     */
    public static String encrypt(String key,String data) {
        AES aes = SecureUtil.aes(StrUtil.bytes(key, CharsetUtil.CHARSET_UTF_8));
        byte[] encrypt = aes.encrypt(data);
        return Base64.encode(encrypt);
    }

    /**
     * 解密
     */
    public static String decrypt(String key,String data) {
        AES aes = SecureUtil.aes(StrUtil.bytes(key, CharsetUtil.CHARSET_UTF_8));
        byte[] decrypt = aes.decrypt(Base64.decode(data));
        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
    }

    public static void main(String[] args) {

        String data = "oeGwLnPy988GlzytD05AazMJQwRbNfenv4HctakLImdavajJN5E242sALApOcgqO";
        System.out.println(data);
        System.out.println(decrypt(data));

    }
}
