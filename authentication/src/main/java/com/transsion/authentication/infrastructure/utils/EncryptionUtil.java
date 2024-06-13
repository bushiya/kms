package com.transsion.authentication.infrastructure.utils;

import com.asa.wbcrypto_dec_wrapper.Wbcrypto_Dec_Wrapper;
import com.asa.wbcrypto_enc_wrapper.Wbcrypto_Enc_Wrapper;
import com.transsion.authentication.infrastructure.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 白盒sdk方法封装
 * @Author wei.chen8
 * @Date 2023/2/27
 */
public class EncryptionUtil {

    /**
     * 加密数据体
     *
     * @param data 数据体
     * @return 加密结果
     */
    public static String encrypt(String data) {
        if(StringUtils.isBlank(data)){
            throw new CustomException("data为空");
        }
        return Wbcrypto_Enc_Wrapper.ASA_Wbcrypto_Encrypt_String_Wrapper(data);
    }

    /**
     * 获取AesKey
     *
     * @param data 数据体
     * @return aesKey
     */
    public static byte[] getAesKey(String data) {
        if(StringUtils.isBlank(data)){
            throw new CustomException("data为空");
        }
        return Wbcrypto_Enc_Wrapper.ASA_Wbcrypto_GenKey_Wrapper(data, 16);
    }


    /**
     * 解密数据体
     *
     * @param data 加密后的数据
     * @return 解密后的数据
     */
    public static String decode(String data) {
        if(StringUtils.isBlank(data)){
            throw new CustomException("data为空");
        }
        return Wbcrypto_Dec_Wrapper.ASA_Wbcrypto_Decrypt_String_Wrapper(data);
    }

    /**
     * 签名
     *
     * @param data 需要签名的信息
     * @return 签名信息
     */
    public static String sign(String data) {
        if(StringUtils.isBlank(data)){
            throw new CustomException("data为空");
        }
        return  Wbcrypto_Dec_Wrapper.ASA_Wbcrypto_Sign_String_Wrapper(data);
    }

    /**
     * 验签
     *
     * @param data 加签名的数据体
     * @param sign 签名信息
     * @return 是否验签成功
     */
    public static Boolean verify(String data, String sign) {
        if(StringUtils.isBlank(data) || StringUtils.isBlank(sign)){
            throw new CustomException("data或sign为空");
        }
        return Wbcrypto_Enc_Wrapper.ASA_Wbcrypto_Verify_String_Wrapper(data, sign);
    }


}
