package com.transsion.authentication.module.auth.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import com.alibaba.fastjson2.JSON;
import com.transsion.authentication.infrastructure.annotation.AppPort;
import com.transsion.authentication.infrastructure.annotation.ServerPort;
import com.transsion.authentication.infrastructure.constants.*;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.utils.*;
import com.transsion.authentication.module.auth.bean.req.*;
import com.transsion.authentication.module.auth.bean.resp.*;
import com.transsion.authentication.module.auth.repository.dao.AppSceneDao;
import com.transsion.authentication.module.auth.repository.dao.DeviceAsymmetrySecretKeyDao;
import com.transsion.authentication.module.auth.repository.dao.DeviceRootSecretKeyDao;
import com.transsion.authentication.module.auth.repository.dao.DeviceSymmetrySecretKeyDao;
import com.transsion.authentication.module.auth.repository.entity.*;
import com.transsion.authentication.module.auth.repository.init.AuthCertApplicationContext;
import com.transsion.authentication.module.auth.repository.init.AuthStorageKeyApplicationContext;
import com.transsion.authentication.module.auth.service.*;
import com.transsion.authentication.module.sys.repository.dao.CertBlacklistDao;
import com.transsion.authentication.module.sys.repository.dao.DeviceAppDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/25
 */
@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    AuthStorageKeyApplicationContext context;

    @Autowired
    AuthCertApplicationContext certApplicationContext;

    @Autowired
    AuthStorageKeyApplicationContext storageKey;

    @Autowired
    HttpServletRequest request;

    @Autowired
    CertBlacklistDao certBlacklistDao;

    @Autowired
    DeviceAppDao deviceAppDao;

    @Autowired
    AppSceneDao appSceneDao;

    @Autowired
    DeviceSymmetrySecretKeyDao deviceSymmetrySecretKeyDao;

    @Autowired
    DeviceSymmetrySecretKeyService deviceSymmetrySecretKeyService;

    @Autowired
    DeviceAsymmetrySecretKeyService deviceAsymmetrySecretKeyService;

    @Autowired
    DeviceAsymmetrySecretKeyDao deviceAsymmetrySecretKeyDao;

    @Autowired
    DeviceRootSecretKeyDao rootSecretKeyDao;

    @Autowired
    AppSceneService appSceneService;

    @Autowired
    ServerService serverService;

    @Autowired
    RooTSecretKeyService rooTSecretKeyService;

    @Autowired
    CertUtil certUtil;

    /**
     * 防止重放攻击
     *
     * @param sessionId    设备app标识
     * @param randomNumber 随机数
     */
    public void preventReplayAttacks(String sessionId, String randomNumber) {
//        String randomHash = String.valueOf(Math.abs((sessionId + randomNumber).hashCode()));
        String andSet = RedisUtil.StringOps.getAndSet(randomNumber, "1");
        if (StringUtils.isBlank(andSet)) {
            boolean expire = RedisUtil.KeyOps.expire(randomNumber, 1, TimeUnit.DAYS);
            if (expire) {
                return;
            }
        }
        throw new CustomException(NetCodeEnum.REPLAY_ATTACK_ERROR);
    }

    /**
     * 建立可信通道
     *
     * @param req 请求体
     * @return
     */
    @AppPort
    @Override
    public SafeChannelResp safeChannel(SafeChannelReq req) {
        String communicationTag = req.getCommunicationTag();
        // 最后16位是 randomNumber
        String sessionId = communicationTag.substring(0, communicationTag.length() - 16);
        String randomNumber = communicationTag.substring(communicationTag.length() - 16);
        // 防重放攻击检测
        preventReplayAttacks(sessionId, randomNumber);

        log.debug("sessionId:{}", sessionId);
        log.debug("randomNumber:{}", randomNumber);
        log.debug("sign:{}", req.getSign());
        // 获取根证书
        X509Certificate rootCert = certApplicationContext.getRootCert();
        // 获取工厂证书、设备证书
        X509Certificate factoryCert = certUtil.getCertByStr(req.getFactoryCert());
        X509Certificate deviceCert = certUtil.getCertByStr(req.getDeviceCert());

        // 根证书校验工厂证书
        certUtil.verifyCert(factoryCert, rootCert.getPublicKey());
        // 工厂证书校验设备证书
        certUtil.verifyCert(deviceCert, factoryCert.getPublicKey());
        // 设备证书验证签名
        log.debug("sha256值：{}", SHAUtils.getSHA256(communicationTag).toUpperCase());
        RSAUtil.verify(communicationTag, deviceCert.getPublicKey(), req.getSign());
        log.debug("签名:{}", "成功");
        // 16位随机数
        String resultRandomNumber = RandomUtil.getRandomNumber(16);
        // 存储随机数 10 分钟
        RedisUtil.StringOps.setEx(RedisKeyPreConst.SAFE_CHANNEL_ + sessionId, resultRandomNumber, 10, TimeUnit.MINUTES);

        SafeChannelResp safeChannelResp = new SafeChannelResp();
        // 公网服务证书
        safeChannelResp.setAuthCert(certApplicationContext.getAuthCertStr());
        // 设备公钥加密后 随机数
        safeChannelResp.setAuthRandomNumber(RsaByteUtil.encryptByPublicKey(resultRandomNumber, deviceCert.getPublicKey().getEncoded()));
        // 公网服务私钥签名随机数
        safeChannelResp.setSign(RsaByteUtil.sign(resultRandomNumber, certApplicationContext.getPrivateKey().getEncoded()));
        return safeChannelResp;
    }

    @AppPort
    @Override
    public SafeChannelTransmitResp safeChannelTransmit(SafeChannelReq req) throws Exception {
        String communicationTag = req.getCommunicationTag();
        // 最后16位是 randomNumber
        String sessionId = communicationTag.substring(0, communicationTag.length() - 16);
        String randomNumber = communicationTag.substring(communicationTag.length() - 16);
        // 防重放攻击检测
        preventReplayAttacks(sessionId, randomNumber);
        // 获取根证书
        X509Certificate rootCert = certApplicationContext.getRootCert();
        // 获取工厂证书、设备证书
        X509Certificate factoryCert = certUtil.getCertByStr(req.getFactoryCert());
        X509Certificate deviceCert = certUtil.getCertByStr(req.getDeviceCert());

        // 根证书校验工厂证书
        certUtil.verifyCert(factoryCert, rootCert.getPublicKey());
        // 工厂证书校验设备证书
        certUtil.verifyCert(deviceCert, factoryCert.getPublicKey());
        PublicKey devicePublicKey = deviceCert.getPublicKey();
        // 设备证书验证签名
        RSAUtil.verify(communicationTag, devicePublicKey, req.getSign());
        log.debug("验签成功");
        // 取出随机数
        String s = RedisUtil.StringOps.get(RedisKeyPreConst.SAFE_CHANNEL_ + sessionId);
        log.debug("验签成功");
        if (StringUtils.isBlank(s)) {
            throw new CustomException(NetCodeEnum.GENERATE_STORAGE_KEY_ERROR);
        }
        RedisUtil.KeyOps.delete(RedisKeyPreConst.SAFE_CHANNEL_ + sessionId);
        String socketKey = RandomUtil.getRandomNumber();
        String macKey = RandomUtil.getRandomNumber();
        log.debug("通信密钥：{}", socketKey);
        log.debug("MAC密钥：{}", macKey);
        DeviceRootSecretKeyEntity rootKey = new DeviceRootSecretKeyEntity();
        rootKey.setSessionId(sessionId);
        //加密存储通信 Key
        String storageKey = context.getStorageKey();
        rootKey.setMacKey(AESUtil.encrypt(macKey, storageKey));
        rootKey.setSocketKey(AESUtil.encrypt(socketKey, storageKey));
        rootKey.setCreateTime(new Date());
        rootKey.setUpdateTime(new Date());
        rooTSecretKeyService.saveUpdate(rootKey);

        SafeChannelTransmitResp resp = new SafeChannelTransmitResp();
        resp.setAppSocketKey(RsaByteUtil.encryptByPublicKey(socketKey, devicePublicKey.getEncoded()));
        resp.setAppSocketKeySign(AESUtil.encrypt(s, socketKey).substring(0, 8));
        resp.setAppMacKey(RsaByteUtil.encryptByPublicKey(macKey, devicePublicKey.getEncoded()));
        resp.setAppMacKeySign(AESUtil.encrypt(s, macKey).substring(0, 8));
        return resp;
    }

    @ServerPort
    @Override
    public EncodeMessageResp encodeMessage(EncodeMessageReq req) throws Exception {

        String appId = request.getHeader(RequestHeadConst.APPID);
        String serverTag = request.getHeader(RequestHeadConst.SERVER_TAG);

        // 查询业务方 通信密钥
        String key = serverService.selectKey(appId, serverTag);
        String sessionId = req.getSessionId();
        String scene = req.getScene();

        // 解密要 加密的消息
        String metaMessage = AESUtil.decrypt(req.getMetaMessage(), key);

        EncodeMessageResp result = new EncodeMessageResp();
        result.setScene(scene);
        // 通信密钥
        if (scene.equals("default")) {
            preventReplayAttacks(req.getSessionId(), req.getRandomNumber());

            DeviceRootSecretKeyEntity rootKey = rootSecretKeyDao.getRootKey(sessionId);
            // 如果超时 删除 key
            if (rootKey.getCreateTime().getTime() + DateUtils.MILLISECONDS_PER_DAY < new Date().getTime()) {
                rootSecretKeyDao.deleteById(sessionId);
                throw new CustomException(NetCodeEnum.STORAGE_KEY_PAST_DUE_ERROR);
            }
            String enMessage = AESUtil.encrypt(metaMessage, AESUtil.decrypt(rootKey.getSocketKey(), context.getStorageKey()));
            String randomNumber = RandomUtil.getRandomNumber(16);
            result.setEnMessage(enMessage);
            result.setRandomNumber(randomNumber);
            result.setAlgorithmTag(AlgorithmEnum.AES.getCode().toString());
            // 加密 消息的 sha256值 取前八位为签名值
            result.setMacMessage(AESUtil.encrypt(SHAUtils.getSHA256(enMessage + randomNumber).toUpperCase(), AESUtil.decrypt(rootKey.getMacKey(), context.getStorageKey())).substring(0, 8));
            return result;
        } else {
            //衍生密钥
            AppSceneEntity appSceneEntity = appSceneDao.selectByAppIdAndScene(appId, req.getScene());
            Integer algorithmCode = appSceneEntity.getAlgorithmCode();
            if (algorithmCode.equals(AlgorithmEnum.AES.getCode())) {
                // AES 对称加密
                // 对称密钥表内获取用户密钥
                DeviceSymmetrySecretKeyEntity deviceSymmetrySecretKeyEntity = deviceSymmetrySecretKeyDao.selectBySessionIdAndScene(sessionId, scene);
                result.setAlgorithmTag(AlgorithmEnum.AES.getCode().toString());
                result.setEnMessage(AESUtil.encrypt(metaMessage, AESUtil.decrypt(deviceSymmetrySecretKeyEntity.getSecretKey(), context.getStorageKey())));
            } else if (algorithmCode.equals(AlgorithmEnum.RSA.getCode())) {
                // RSA 非对称加密
                // 非对称表内获取用户密钥
                DeviceAsymmetrySecretKeyEntity deviceAsymmetrySecretKeyEntity = deviceAsymmetrySecretKeyDao.selectBySessionIdAndScene(sessionId, scene);
                result.setAlgorithmTag(AlgorithmEnum.RSA.getCode().toString());
                String enMessage = RsaByteUtil.encryptByPublicKey(metaMessage, Base64Decoder.decode(AESUtil.decrypt(deviceAsymmetrySecretKeyEntity.getPhonePublicKey(), context.getStorageKey())));
                result.setEnMessage(enMessage);
            } else {
                throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
            }
        }
        return result;
    }

    @ServerPort
    @Override
    public String decodeMessage(DecodeMessageReq req) throws Exception {

        String appId = request.getHeader(RequestHeadConst.APPID);
        String serverTag = request.getHeader(RequestHeadConst.SERVER_TAG);
        // 查询业务方 通信密钥
        String key = serverService.selectKey(appId, serverTag);
        String sessionId = req.getSessionId();
        String scene = req.getScene();
        String metaMessage = req.getEnMessage();

        // 通信密钥
        if (scene.equals("default")) {
            preventReplayAttacks(req.getSessionId(), req.getRandomNumber());
            DeviceRootSecretKeyEntity rootKey = rootSecretKeyDao.getRootKey(sessionId);
            // 如果超时 删除 key
            if (rootKey.getCreateTime().getTime() + DateUtils.MILLISECONDS_PER_DAY < new Date().getTime()) {
                rootSecretKeyDao.deleteById(sessionId);
                throw new CustomException(NetCodeEnum.STORAGE_KEY_PAST_DUE_ERROR);
            }
            log.debug("存储密钥{}", context.getStorageKey());
            String storageKey = AESUtil.decrypt(rootKey.getSocketKey(), context.getStorageKey());
            String sign = AESUtil.encrypt(SHAUtils.getSHA256(metaMessage + req.getRandomNumber()).toUpperCase(), AESUtil.decrypt(rootKey.getMacKey(), context.getStorageKey())).substring(0, 8);
            if (!req.getMacMessage().equals(sign)) {
                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
            }
            metaMessage = AESUtil.decrypt(metaMessage, storageKey);
            return AESUtil.encrypt(metaMessage, key);
        } else {
            //衍生密钥
            AppSceneEntity appSceneEntity = appSceneDao.selectByAppIdAndScene(appId, req.getScene());
            Integer algorithmCode = appSceneEntity.getAlgorithmCode();
            if (algorithmCode.equals(AlgorithmEnum.AES.getCode())) {
                // AES 对称加密
                // 对称密钥表内获取用户密钥
                DeviceSymmetrySecretKeyEntity deviceSymmetrySecretKeyEntity = deviceSymmetrySecretKeyDao.selectBySessionIdAndScene(sessionId, scene);
                log.debug("解密开始：{}", metaMessage);
                String data = AESUtil.decrypt(metaMessage, AESUtil.decrypt(deviceSymmetrySecretKeyEntity.getSecretKey(), context.getStorageKey()));
                log.debug("解密成功");
                // 使用业务方密钥加密消息
                return AESUtil.encrypt(data, key);
            } else if (algorithmCode.equals(AlgorithmEnum.RSA.getCode())) {
                // RSA 非对称加密
                // 非对称表内获取用户密钥
                DeviceAsymmetrySecretKeyEntity deviceAsymmetrySecretKeyEntity = deviceAsymmetrySecretKeyDao.selectBySessionIdAndScene(sessionId, scene);
                String data = RsaByteUtil.decryptByPrivateKey(metaMessage, Base64Decoder.decode(AESUtil.decrypt(deviceAsymmetrySecretKeyEntity.getServerPrivateKey(), context.getStorageKey())));
                return AESUtil.encrypt(data, key);
            } else {
                throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
            }
        }
    }

    @ServerPort
    @Override
    public SignResp signMessage(SignMessageReq req) throws Exception {
        String appId = request.getHeader(RequestHeadConst.APPID);
        String serverTag = request.getHeader(RequestHeadConst.SERVER_TAG);

        // 查询业务方 通信密钥
        String key = serverService.selectKey(appId, serverTag);
        String sessionId = req.getSessionId();
        String scene = req.getScene();
        String metaMessage = AESUtil.decrypt(req.getMetaMessage(), key);
        SignResp resp = new SignResp();
        // 通信密钥
        if (scene.equals("default")) {
            throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
        } else {
            //衍生密钥
            AppSceneEntity appSceneEntity = appSceneDao.selectByAppIdAndScene(appId, req.getScene());
            Integer algorithmCode = appSceneEntity.getAlgorithmCode();
            if (algorithmCode.equals(AlgorithmEnum.AES.getCode())) {
                // AES 不支持签名
                throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
            } else if (algorithmCode.equals(AlgorithmEnum.RSA.getCode())) {
                resp.setScene(scene);
                resp.setAlgorithmTag(AlgorithmEnum.RSA.getCode().toString());
                // RSA 非对称签名
                // 非对称表内获取用户密钥
                DeviceAsymmetrySecretKeyEntity deviceAsymmetrySecretKeyEntity = deviceAsymmetrySecretKeyDao.selectBySessionIdAndScene(sessionId, scene);
                String sign = RsaByteUtil.sign(metaMessage, Base64Decoder.decode(AESUtil.decrypt(deviceAsymmetrySecretKeyEntity.getServerPrivateKey(), context.getStorageKey())));
                resp.setSign(sign);
                return resp;
            } else {
                throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
            }
        }
    }

    @ServerPort
    @Override
    public Boolean verifySign(VerifySignReq req) throws Exception {
        String appId = request.getHeader(RequestHeadConst.APPID);
        String serverTag = request.getHeader(RequestHeadConst.SERVER_TAG);

        // 查询业务方 通信密钥
        String key = serverService.selectKey(appId, serverTag);
        String sessionId = req.getSessionId();
        String scene = req.getScene();
        String metaMessage = AESUtil.decrypt(req.getMetaMessage(), key);
        String sign = req.getMessageSign();

        // 通信密钥
        if (scene.equals("default")) {
            throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
        } else {
            //衍生密钥
            AppSceneEntity appSceneEntity = appSceneDao.selectByAppIdAndScene(appId, req.getScene());
            Integer algorithmCode = appSceneEntity.getAlgorithmCode();
            if (algorithmCode.equals(AlgorithmEnum.AES.getCode())) {
                // AES 不支持验签
                throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
            } else if (algorithmCode.equals(AlgorithmEnum.RSA.getCode())) {
                // RSA 非对称验签
                // 非对称表内获取用户密钥
                DeviceAsymmetrySecretKeyEntity deviceAsymmetrySecretKeyEntity = deviceAsymmetrySecretKeyDao.selectBySessionIdAndScene(sessionId, scene);
                String phonePublicKey = AESUtil.decrypt(deviceAsymmetrySecretKeyEntity.getPhonePublicKey(), context.getStorageKey());
                RSAUtil.verify(metaMessage, phonePublicKey, sign);
                return Boolean.TRUE;
            } else {
                throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
            }
        }
    }

    @AppPort
    @Override
    public Object derivedKey(DerivedKeyReq req) throws Exception {
        preventReplayAttacks(req.getSessionId(), req.getRandomNumber());

        String appId = request.getHeader(RequestHeadConst.APPID);
        String serverTag = request.getHeader(RequestHeadConst.SERVER_TAG);

        String sessionId = req.getSessionId();
        // 查询通信密钥
        DeviceRootSecretKeyEntity rootKey = rootSecretKeyDao.getRootKey(sessionId);
        String macKey = AESUtil.decrypt(rootKey.getMacKey(), context.getStorageKey());
        String socketKey = AESUtil.decrypt(rootKey.getSocketKey(), context.getStorageKey());
        // 使用 MAC 密钥验签
        String sign = AESUtil.encrypt(SHAUtils.getSHA256(req.getEncodeKeyIndex() + req.getRandomNumber()).toUpperCase(), macKey).substring(0, 8);
        log.debug("sign:{}", sign);
        if (!sign.toString().equals(req.getMac())) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }

        // keyIndex JSON 字符串
        String keyIndexStr = AESUtil.decrypt(req.getEncodeKeyIndex(), socketKey);
        keyIndexStr = RSAUtil.ketToJson(keyIndexStr);
        log.debug("keyIndexStr:{}", keyIndexStr);

        KeyIndex keyIndex = JSON.parseObject(keyIndexStr, KeyIndex.class);
        log.debug("keyIndex:{}", keyIndex);
        // 衍生密钥算法标识
        Integer algorithmCode = keyIndex.getAlgo();

        //  判断是否已有此场景 向 App 场景表内加入
        // TODO 查询业务方密钥长度 判别是否可以继续衍生
        AppSceneEntity appSceneEntity = new AppSceneEntity();
        appSceneEntity.setAppId(request.getHeader(RequestHeadConst.APPID));
        appSceneEntity.setScene(keyIndex.getSc());
        appSceneEntity.setState(0);
        appSceneEntity.setAlgorithmCode(keyIndex.getAlgo());
        appSceneService.isHasAndSave(appSceneEntity);

        if (algorithmCode.equals(AlgorithmEnum.AES.getCode())) {
            // 衍生的 AES 密钥
            String secretKey = AESUtil.encrypt(RandomUtil.getRandomNumber(16), RandomUtil.getRandomNumber(16)).substring(0, 16);
            String randomNumber = RandomUtil.getRandomNumber(16);
            // KMS 托管
            if (keyIndex.getKLoc().equals(TrusteeEnum.KMS.getCode())) {
                DeviceSymmetrySecretKeyEntity data = new DeviceSymmetrySecretKeyEntity();
                data.setScene(keyIndex.getSc());
                data.setSessionId(sessionId);
                // 加密存储衍生密钥
                data.setSecretKey(AESUtil.encrypt(secretKey, context.getStorageKey()));
                data.setCreateTime(new Date());
                data.setUpdateTime(new Date());
                deviceSymmetrySecretKeyService.saveOrUpdateSecretKey(data);

                secretKey = AESUtil.encrypt(secretKey, socketKey);
                DerivedKeyResp.SymmetryKmsScene resp = new DerivedKeyResp.SymmetryKmsScene();
                resp.setAppSocketKey(secretKey);
                resp.setRandomNumber(randomNumber);
                resp.setAppSocketKeySign(AESUtil.encrypt(SHAUtils.getSHA256(secretKey + randomNumber).toUpperCase(), macKey).substring(0, 8));
                log.debug("resp:{}", resp);
                return resp;
            } else if (keyIndex.getKLoc().equals(TrusteeEnum.ONESELF.getCode())) {
                // 查询业务方 通信密钥
                String key = serverService.selectKey(appId, serverTag);
                secretKey = AESUtil.encrypt(secretKey, socketKey);
                // 业务方自己托管
                DerivedKeyResp.SymmetryScene resp = new DerivedKeyResp.SymmetryScene();
                resp.setAppSocketKey(secretKey);
                resp.setRandomNumber(randomNumber);
                resp.setAppSocketKeySign(AESUtil.encrypt(SHAUtils.getSHA256(secretKey + randomNumber).toUpperCase(), macKey).substring(0, 8));
                resp.setSeverSocketKey(AESUtil.encrypt(secretKey, key));
                return resp;
            } else {
                throw new CustomException(NetCodeEnum.TRUSTEE_ERROR);
            }
        } else if (algorithmCode.equals(AlgorithmEnum.RSA.getCode())) {
            // KMS 托管
            if (keyIndex.getKLoc().equals(TrusteeEnum.KMS.getCode())) {
                String devicePublicKey = keyIndex.getDPub();
                if (StringUtils.isBlank(devicePublicKey)) {
                    throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
                }
//                devicePublicKey=AES.decrypt(devicePublicKey,)
                // RSA 衍生密钥
                Map<String, String> map = RSAUtil.initRSAKey(2048);
                String publicKey = map.get("publicKey");
                String privateKey = map.get("privateKey");

                DeviceAsymmetrySecretKeyEntity data = new DeviceAsymmetrySecretKeyEntity();
                data.setSessionId(sessionId);
                data.setScene(keyIndex.getSc());
                data.setPhonePublicKey(AESUtil.encrypt(devicePublicKey, context.getStorageKey()));
                data.setServerPublicKey(AESUtil.encrypt(publicKey, context.getStorageKey()));
                data.setServerPrivateKey(AESUtil.encrypt(privateKey, context.getStorageKey()));
                data.setCreateTime(new Date());
                data.setUpdateTime(new Date());
                deviceAsymmetrySecretKeyService.saveOrUpdateSecretKey(data);
                log.debug("已添加");
                DerivedKeyResp.AsymmetryKmsScene resp = new DerivedKeyResp.AsymmetryKmsScene();
                String randomNumber = RandomUtil.getRandomNumber(16);
                resp.setServerPublicKey(publicKey);
                resp.setRandomNumber(randomNumber);

                // mac密钥加密的 sha256(serverPublicKey+randomNumber).subString(0,8)
                resp.setServerPublicKeySign(AESUtil.encrypt(SHAUtils.getSHA256(publicKey + randomNumber).toUpperCase(), macKey).substring(0, 8));
                return resp;
            } else if (keyIndex.getKLoc().equals(TrusteeEnum.ONESELF.getCode())) {
                // 查询业务方 通信密钥
                String key = serverService.selectKey(appId, serverTag);
                // 业务方自己托管
                DerivedKeyResp.AsymmetryScene resp = new DerivedKeyResp.AsymmetryScene();
                resp.setAppPublicKey(AESUtil.encrypt(keyIndex.getDPub(), key));
                return resp;
            } else {
                throw new CustomException(NetCodeEnum.TRUSTEE_ERROR);
            }
        } else {
            throw new CustomException(NetCodeEnum.ALGORITHM_ERROR);
        }
    }

}
