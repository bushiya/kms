package com.transsion.authenticationsdk.module.safety.service.impl;

import com.alibaba.fastjson2.JSON;
import com.transsion.authenticationsdk.infrastructure.advice.CommResponse;
import com.transsion.authenticationsdk.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationsdk.infrastructure.constants.RequestHeadConst;
import com.transsion.authenticationsdk.infrastructure.constants.RequestUriConst;
import com.transsion.authenticationsdk.infrastructure.core.KmsClient;
import com.transsion.authenticationsdk.infrastructure.exception.SdkCustomException;
import com.transsion.authenticationsdk.infrastructure.utils.HttpRequestUtil;
import com.transsion.authenticationsdk.module.safety.body.*;
import com.transsion.authenticationsdk.module.safety.service.SafetyService;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class SafetyServiceImpl implements SafetyService {
    /**
     * 加密消息
     *
     * @param kmsClient 业务方身份信息
     * @param req       加密请求体
     * @return
     */
    @Override
    public String encrypt(KmsClient kmsClient, EncryptReq req) {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(RequestHeadConst.APPID, kmsClient.getAppId());
            headers.put(RequestHeadConst.SERVER_TAG, kmsClient.getServerTag());
            CommResponse s = HttpRequestUtil.postHttp(kmsClient.getKmsUrl() + RequestUriConst.ENCRYPT, JSON.toJSONString(req), headers);
            if (s.getCode() != NetCodeEnum.SUCCESS.getCode()) {
                throw new SdkCustomException(s.getCode(), s.getMessage());
            }
            kmsClient.getSdkLog().log(Level.INFO, "KMS 加密结果：{0}", JSON.toJSONString(s));
            return s.getData().toString();
        } catch (Exception e) {
            kmsClient.getSdkLog().log(Level.INFO, "KMS 加密异常：{0}", e.getMessage());
            throw new SdkCustomException(NetCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }

    @Override
    public String decrypt(KmsClient kmsClient, DecryptReq req) {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(RequestHeadConst.APPID, kmsClient.getAppId());
            headers.put(RequestHeadConst.SERVER_TAG, kmsClient.getServerTag());
            CommResponse s = HttpRequestUtil.postHttp(kmsClient.getKmsUrl() + RequestUriConst.DECRYPT, JSON.toJSONString(req), headers);
            if (s.getCode() != NetCodeEnum.SUCCESS.getCode()) {
                throw new SdkCustomException(s.getCode(), s.getMessage());
            }
            kmsClient.getSdkLog().log(Level.INFO, "KMS 解密结果：{0}", JSON.toJSONString(s));
            return s.getData().toString();
        } catch (Exception e) {
            kmsClient.getSdkLog().log(Level.INFO, "KMS 解密异常：{0}", e.getMessage());
            throw new SdkCustomException(NetCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }

    @Override
    public String sign(KmsClient kmsClient, SignReq req) {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(RequestHeadConst.APPID, kmsClient.getAppId());
            headers.put(RequestHeadConst.SERVER_TAG, kmsClient.getServerTag());
            CommResponse s = HttpRequestUtil.postHttp(kmsClient.getKmsUrl() + RequestUriConst.SIGN, JSON.toJSONString(req), headers);
            if (s.getCode() != NetCodeEnum.SUCCESS.getCode()) {
                throw new SdkCustomException(s.getCode(), s.getMessage());
            }
            kmsClient.getSdkLog().log(Level.INFO, "KMS 签名结果：{0}", JSON.toJSONString(s));
            return s.getData().toString();
        } catch (Exception e) {
            kmsClient.getSdkLog().log(Level.INFO, "KMS 签名异常：{0}", e.getMessage());
            throw new SdkCustomException(NetCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }

    @Override
    public boolean verify(KmsClient kmsClient, VerifyReq req) {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(RequestHeadConst.APPID, kmsClient.getAppId());
            headers.put(RequestHeadConst.SERVER_TAG, kmsClient.getServerTag());
            CommResponse s = HttpRequestUtil.postHttp(kmsClient.getKmsUrl() + RequestUriConst.VERIFY, JSON.toJSONString(req), headers);
            if (s.getCode() != NetCodeEnum.SUCCESS.getCode()) {
                throw new SdkCustomException(s.getCode(), s.getMessage());
            }
            kmsClient.getSdkLog().log(Level.INFO, "KMS 验签结果：{0}", JSON.toJSONString(s));
            return Boolean.parseBoolean(s.getData().toString());
        } catch (Exception e) {
            kmsClient.getSdkLog().log(Level.INFO, "KMS 验签异常：{0}", e.getMessage());
            throw new SdkCustomException(NetCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }

    @Override
    public String derivedKey(KmsClient kmsClient, DerivedKey req) {
        try {
            HashMap<String, String> headers = new HashMap<>();
            headers.put(RequestHeadConst.APPID, kmsClient.getAppId());
            headers.put(RequestHeadConst.SERVER_TAG, kmsClient.getServerTag());
            CommResponse s = HttpRequestUtil.postHttp(kmsClient.getKmsUrl() + RequestUriConst.DERIVED, JSON.toJSONString(req), headers);
            if (s.getCode() != NetCodeEnum.SUCCESS.getCode()) {
                throw new SdkCustomException(s.getCode(), s.getMessage());
            }
            kmsClient.getSdkLog().log(Level.INFO, "KMS 衍生密钥结果：{0}", JSON.toJSONString(s));
            return s.getData().toString();
        } catch (Exception e) {
            kmsClient.getSdkLog().log(Level.INFO, "KMS 衍生密钥异常：{0}", e.getMessage());
            throw new SdkCustomException(NetCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }
}
