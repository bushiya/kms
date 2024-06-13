package com.transsion.authenticationsdk.infrastructure.core;

import com.alibaba.fastjson2.JSON;
import com.transsion.authenticationsdk.infrastructure.exception.SdkCustomException;
import com.transsion.authenticationsdk.infrastructure.utils.AESUtil;
import com.transsion.authenticationsdk.infrastructure.utils.RandomUtil;
import com.transsion.authenticationsdk.infrastructure.utils.StringUtil;
import com.transsion.authenticationsdk.module.auth.service.ServerAuthService;
import com.transsion.authenticationsdk.module.auth.service.impl.ServiceAuthServiceImpl;
import com.transsion.authenticationsdk.module.safety.body.*;
import com.transsion.authenticationsdk.module.safety.service.impl.SafetyServiceImpl;

import java.util.logging.Logger;

/**
 * @Description: Kms sdk 核心方法
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
public class KmsClient {

    //业务方 appId
    private String appId;

    //Kms Url
    private String kmsUrl;

    //Kms 通信公钥
    private String publicKey;

    //Kms 通信私钥
    private String privateKey;

    //Kms 接入下发的公钥
    private String kmsPublicKey;

    // 服务标识,区分集群
    private String serverTag;

    // 通信密钥
    private String secretKey;

    private Logger sdkLog = Logger.getLogger(this.getClass().getName());

    private RuntimeService runtimeService = new RuntimeService();

    static class RuntimeService {
        private ServerAuthService serverAuthService = new ServiceAuthServiceImpl();
        private SafetyServiceImpl safetyService = new SafetyServiceImpl();
    }

    public String encode(String data) throws Exception {
        if (StringUtil.isBlank(secretKey)) {
            runtimeService.serverAuthService.initServer(this);
        }
        return AESUtil.encrypt(data, secretKey);
    }

    public String encrypt(String sessionId, String scene, String data) throws SdkCustomException {
        if (StringUtil.isBlank(secretKey)) {
            runtimeService.serverAuthService.initServer(this);
        }
        assert !StringUtil.isBlank(sessionId) : "缺失参数:sessionId";
        assert !StringUtil.isBlank(scene) : "缺失参数:scene";
        assert !StringUtil.isBlank(data) : "缺失参数:data";
        EncryptReq req = new EncryptReq();
        req.setSessionId(sessionId);
        req.setScene(scene);
        req.setMetaMessage(AESUtil.encrypt(data, secretKey));
        req.setRandomNumber(RandomUtil.randomSequence(16));
        return runtimeService.safetyService.encrypt(this, req);
    }

    public String decrypt(String sessionId, String data) throws SdkCustomException {
        if (StringUtil.isBlank(secretKey)) {
            runtimeService.serverAuthService.initServer(this);
        }
        assert !StringUtil.isBlank(sessionId) : "缺失参数:sessionId";
        assert !StringUtil.isBlank(data) : "缺失参数:data";
        DecryptReq req = new DecryptReq();
        req = JSON.parseObject(data, DecryptReq.class);
        req.setSessionId(sessionId);
        return AESUtil.decrypt(runtimeService.safetyService.decrypt(this, req), secretKey);
    }

    public String sign(String sessionId, String scene, String data) throws SdkCustomException {
        if (StringUtil.isBlank(secretKey)) {
            runtimeService.serverAuthService.initServer(this);
        }
        assert !StringUtil.isBlank(sessionId) : "缺失参数:sessionId";
        assert !StringUtil.isBlank(scene) : "缺失参数:scene";
        assert !StringUtil.isBlank(data) : "缺失参数:data";
        SignReq req = new SignReq();
        req.setSessionId(sessionId);
        req.setScene(scene);
        req.setMetaMessage(AESUtil.encrypt(data, secretKey));
        return runtimeService.safetyService.sign(this, req);
    }

    public boolean verify(String sessionId, String scene, String data, String sign) throws SdkCustomException {
        if (StringUtil.isBlank(secretKey)) {
            runtimeService.serverAuthService.initServer(this);
        }
        assert !StringUtil.isBlank(sessionId) : "缺失参数:sessionId";
        assert !StringUtil.isBlank(scene) : "缺失参数:scene";
        assert !StringUtil.isBlank(data) : "缺失参数:data";
        assert !StringUtil.isBlank(sign) : "缺失参数:sign";
        VerifyReq req = new VerifyReq();
        req.setSessionId(sessionId);
        req.setScene(scene);
        req.setMetaMessage(AESUtil.encrypt(data, secretKey));
        req.setMessageSign(sign);

        return runtimeService.safetyService.verify(this, req);
    }

    public String derivedSecretKey(String sessionId, String keyIndex) {
        if (StringUtil.isBlank(secretKey)) {
            runtimeService.serverAuthService.initServer(this);
        }
        assert StringUtil.isBlank(sessionId) : "缺失参数:sessionId";
        assert StringUtil.isBlank(keyIndex) : "缺失参数:keyIndex";
        DerivedKey req = JSON.parseObject(keyIndex, DerivedKey.class);
        req.setSessionId(sessionId);
        return runtimeService.safetyService.derivedKey(this, req);
    }


    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getKmsUrl() {
        return this.kmsUrl;
    }

    public void setKmsUrl(String kmsUrl) {
        this.kmsUrl = kmsUrl;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getKmsPublicKey() {
        return kmsPublicKey;
    }

    public void setKmsPublicKey(String kmsPublicKey) {
        this.kmsPublicKey = kmsPublicKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getServerTag() {
        return serverTag;
    }

    public void setServerTag(String serverTag) {
        this.serverTag = serverTag;
    }

    public Logger getSdkLog() {
        return sdkLog;
    }
//    {
//        //获取业务方appId
//        String serviceId = PropertiesUtils.getValue(KMS_APPID);
//        if (StringUtil.isNotBlank(serviceId)) {
//            appId = serviceId;
//        }
//        //获取业务方appId
//        String url = PropertiesUtils.getValue(KMS_URL);
//        if (StringUtil.isNotBlank(url)) {
//            kmsUrl = url;
//        }
//    }

    @Override
    public String toString() {
        return "KmsClient{" +
                "appId='" + appId + '\'' +
                ", kmsUrl='" + kmsUrl + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", kmsPublicKey='" + kmsPublicKey + '\'' +
                ", runtimeService=" + runtimeService +
                '}';
    }
}
