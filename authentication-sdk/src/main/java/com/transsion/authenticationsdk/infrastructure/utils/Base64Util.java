package com.transsion.authenticationsdk.infrastructure.utils;

import java.util.Base64;

/**
 * @Description: Base64 工具类
 * @Author jiakang.chen
 * @Date 2023/7/11
 */
public class Base64Util {

    public static byte[] decode(String base64) {
        return decode(base64.getBytes());
    }

    public static byte[] decode(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    public static String encode(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    public static String encode(String data) {
        if (data == null) {
            return null;
        }
        return encode(data.getBytes());
    }
}
