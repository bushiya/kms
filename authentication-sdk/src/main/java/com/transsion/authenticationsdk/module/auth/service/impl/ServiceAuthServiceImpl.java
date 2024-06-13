package com.transsion.authenticationsdk.module.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.transsion.authenticationsdk.infrastructure.advice.CommResponse;
import com.transsion.authenticationsdk.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationsdk.infrastructure.constants.RequestHeadConst;
import com.transsion.authenticationsdk.infrastructure.constants.RequestUriConst;
import com.transsion.authenticationsdk.infrastructure.core.KmsClient;
import com.transsion.authenticationsdk.infrastructure.exception.SdkCustomException;
import com.transsion.authenticationsdk.infrastructure.utils.HttpRequestUtil;
import com.transsion.authenticationsdk.infrastructure.utils.RSAUtil;
import com.transsion.authenticationsdk.infrastructure.utils.RandomUtil;
import com.transsion.authenticationsdk.module.auth.body.ServerAuthReq;
import com.transsion.authenticationsdk.module.auth.body.ServerAuthResp;
import com.transsion.authenticationsdk.module.auth.service.ServerAuthService;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @Description: 业务方初始化与 KMS服务 建立安全通道 功能实现
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class ServiceAuthServiceImpl implements ServerAuthService {
    /**
     * 业务方与 KMS 服务器 建立安全通道
     *
     * @param kmsClient 业务方核心参数
     * @return
     */
    @Override
    public KmsClient initServer(KmsClient kmsClient) {
        try {
            String serverTag = RandomUtil.randomSequence(19) + System.currentTimeMillis();
            kmsClient.setServerTag(serverTag);
            Map<String, String> headers = new HashMap<>();
            headers.put(RequestHeadConst.APPID, kmsClient.getAppId());
            ServerAuthReq req = new ServerAuthReq();
            req.setRandomNumber(serverTag);
            req.setVerifyMessage(RSAUtil.sign(serverTag, kmsClient.getPrivateKey()));

            CommResponse s = HttpRequestUtil.postHttp(kmsClient.getKmsUrl() + RequestUriConst.INIT, JSON.toJSONString(req), headers);
            if (s.getCode() != NetCodeEnum.SUCCESS.getCode()) {
                throw new SdkCustomException(s.getCode(), s.getMessage());
            }
            kmsClient.getSdkLog().log(Level.INFO, "业务方与 KMS 服务器 建立安全通道成功：{0}", JSON.toJSONString(s));
            ServerAuthResp data = JSON.parseObject(s.getData().toString(), ServerAuthResp.class);
            String secretKeyEn = data.getSecretKey();
            String sign = data.getSign();
            String secretKey = RSAUtil.decryptByPrivateKey(secretKeyEn, kmsClient.getPrivateKey());
            RSAUtil.verify(secretKey, kmsClient.getKmsPublicKey(), sign);
            kmsClient.setSecretKey(secretKey);
            return kmsClient;
        } catch (Exception e) {
            kmsClient.getSdkLog().log(Level.INFO, "业务方与 KMS 服务器 建立安全通道失败,错误原因：{0}", e.toString());
            throw new SdkCustomException(NetCodeEnum.KMS_AUTH_CHANNEL);
        }
    }
}
