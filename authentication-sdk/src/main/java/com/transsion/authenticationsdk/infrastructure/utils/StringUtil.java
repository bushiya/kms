package com.transsion.authenticationsdk.infrastructure.utils;

/**
 * @Description: String 工具类
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class StringUtil {
    /**
     * 判断字符串是否为 Null
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为 Null
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
