package com.transsion.authentication.module.da.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.transsion.authentication.infrastructure.constants.ModelEnum;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.utils.RSAUtils;
import com.transsion.authentication.infrastructure.utils.RsaHelper;
import com.transsion.authentication.infrastructure.utils.SHAUtils;
import com.transsion.authentication.module.da.controller.req.AuthRecordReq;
import com.transsion.authentication.module.da.controller.req.DaReq;
import com.transsion.authentication.module.da.controller.rsp.DaResp;
import com.transsion.authentication.module.da.controller.rsp.DaZrResp;
import com.transsion.authentication.module.da.repository.entity.AfterSaveEntity;
import com.transsion.authentication.module.da.repository.entity.DaEntity;
import com.transsion.authentication.module.da.repository.entity.FansEntity;
import com.transsion.authentication.module.da.repository.mapper.DaMapper;
import com.transsion.authentication.module.da.repository.resource.AuthKeyResource;
import com.transsion.authentication.module.da.service.AuthService;
import com.transsion.authentication.module.da.service.DaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Date;

@Slf4j
@Service
public class DaServiceImpl extends ServiceImpl<DaMapper, DaEntity> implements DaService {
    @Autowired
    HttpServletRequest request;

    @Autowired
    AuthKeyResource authKeyResource;

    @Autowired
    DaMapper daMapper;

    @Autowired
    AuthService authServicel;

//    @Override
//    public DaResp mtkDownloadVerify(DaReq da) throws Exception {
//        String chipId = request.getHeader("Chip-ID");
//        String chipPlatform = request.getHeader("Chip-Platform");
//        String toolVersion = request.getHeader("Tool-Version");
//        String projectVersion = request.getHeader("Project-Version");
//        String upgradeModel = request.getHeader("Upgrade-Model");
//        String softwareVersion = request.getHeader("Software-Version");
//        String deviceModel = request.getHeader("Device-Model");
//        String deviceBrand = request.getHeader("Device-Brand");
//        String workOrderID = request.getHeader("Work-Order-ID");
//        log.info("mtkDownloadVerify:[chipId:{},deviceModel:{}]", chipId, deviceModel);
//        DaEntity daEntity = new DaEntity();
//        daEntity.setChipId(chipId);
//        daEntity.setChipPlatform(chipPlatform);
//        daEntity.setToolVersion(toolVersion);
//        daEntity.setCreateTime(new Date());
//        daEntity.setProjectVersion(projectVersion);
//        daEntity.setToolType(upgradeModel);
//        daEntity.setSoftwareVersion(softwareVersion);
//        daEntity.setDeviceModel(deviceModel);
//        daEntity.setDeviceBrand(deviceBrand);
//        daEntity.setWorkOrderID(workOrderID);
//
//        //场景验证
//        Integer scene = da.getScene();
//        daEntity.setScene(scene);
//        if (scene.equals(ModelEnum.FACTORY.getCode())) {
////            log.info("特征码校验开始:{}", featureCode);
////            AfterSaveEntity afterSaveEntity = authServicel.featureCodeAuth(featureCode);
////            log.info("特征码参数:{}", afterSaveEntity);
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 售后版本 特征码
//                 */
//                String featureCode = da.getVerifyData();
//                daEntity.setFeatureCode(featureCode);
//                daMapper.insert(daEntity);
//            }
//        } else if (scene.equals(ModelEnum.RESEARCH.getCode())) {
////            String[] data = da.getVerifyData().split(",");
////            authServicel.oaTokenAuth(data[0], data[1]);
//            if (StringUtils.isNotBlank(chipId)) {
//                daMapper.insert(daEntity);
//            }
//        } else if (scene.equals(ModelEnum.FANS.getCode())) {
//            FansEntity fansEntity = authServicel.palmIdAuth(da.getVerifyData());
//            daEntity.setAccountInformation(fansEntity.getUniqueId());
//            if (StringUtils.isNotBlank(chipId)) {
//                daMapper.insert(daEntity);
//            }
//        } else if (scene.equals(ModelEnum.AFTET_SAVE.getCode())) {
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 售后版本 特征码
//                 */
//                String featureCode = da.getVerifyData();
//                daEntity.setFeatureCode(featureCode);
//                daMapper.insert(daEntity);
//            }
//        } else {
//            throw new CustomException(NetCodeEnum.SCENE_FAIL);
//        }
//
//        //数据验证
//        String randomNumber = "";
//        try {
//            log.info("开始验签");
//            log.info("random:{}", da.getEncodeRandomNumber());
//            log.info("sign:{}", da.getSign());
//            log.info("publicKey:{}", authKeyResource.getPcPublicKey());
//            //验签 Base64转码后的数据
//            boolean verify = RSAUtils.verify(da.getEncodeRandomNumber().getBytes(), authKeyResource.getPcPublicKey(), da.getSign());
//            log.info("验签结果为:{}", verify);
//            if (verify) {
//                log.info("开始解密");
//                randomNumber = RSAUtils.decryptByPrivateKey(authKeyResource.getServerPrivateKey(), da.getEncodeRandomNumber(), RSAUtils.RSA_LENGTH_2048);
//                log.info("解密的随机数为:{}", (new String(Base64Decoder.decode(randomNumber))));
//                log.info("解密的随机数为:{}", (new String(Base64Decoder.decode(randomNumber.getBytes()))));
//                log.info("解密的随机数为:{}", ArrayUtils.toString(Base64Decoder.decode(randomNumber.getBytes())));
//                log.info("解密的随机数为:{}", ArrayUtils.toString(Base64Decoder.decode(randomNumber)));
//                if (StringUtils.isBlank(randomNumber)) {
//                    log.info("解密失败");
//                    throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
//                }
//            } else {
//                log.info("验签失败");
//                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
//            }
//            log.info("开始执行流程");
//            log.info("decode后的长度{}", Base64Decoder.decode(randomNumber).length);
//            byte[] encodePhone = RSAUtils.rsaEncryptionOaepSha256ByPrivate(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), Base64Decoder.decode(randomNumber));
//            String sign = RSAUtils.sign(Base64Encoder.encode(encodePhone).getBytes(), authKeyResource.getServerPrivateKey());
//
//            DaResp daResp = new DaResp();
//            daResp.setSign(sign);
//            daResp.setEncodePc(Base64Encoder.encode(encodePhone));
//            return daResp;
//        } catch (Exception e) {
//            log.info("失败:{}", e.getMessage());
//            throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
//        }
//    }
//
//    @Override
//    public DaZrResp sprdFdlVerify(DaReq da) {
//        String chipId = request.getHeader("Chip-ID");
//        String chipPlatform = request.getHeader("Chip-Platform");
//        String toolVersion = request.getHeader("Tool-Version");
//        String projectVersion = request.getHeader("Project-Version");
//        String upgradeModel = request.getHeader("Upgrade-Model");
//        String softwareVersion = request.getHeader("Software-Version");
//        String deviceModel = request.getHeader("Device-Model");
//        String deviceBrand = request.getHeader("Device-Brand");
//        String workOrderID = request.getHeader("Work-Order-ID");
//        log.info("sprdFdlVerify:[chipId:{},deviceModel:{}]", chipId, deviceModel);
//        DaEntity daEntity = new DaEntity();
//        daEntity.setChipId(chipId);
//        daEntity.setChipPlatform(chipPlatform);
//        daEntity.setToolVersion(toolVersion);
//        daEntity.setCreateTime(new Date());
//        daEntity.setProjectVersion(projectVersion);
//        daEntity.setToolType(upgradeModel);
//        daEntity.setSoftwareVersion(softwareVersion);
//        daEntity.setDeviceModel(deviceModel);
//        daEntity.setDeviceBrand(deviceBrand);
//        daEntity.setWorkOrderID(workOrderID);
//
//        log.info("展锐请求头参数:{}", daEntity);
//        //场景验证
//        Integer scene = da.getScene();
//        daEntity.setScene(scene);
//        if (scene.equals(ModelEnum.FACTORY.getCode())) {
////            log.info("特征码校验开始:{}", featureCode);
////            AfterSaveEntity afterSaveEntity = authServicel.featureCodeAuth(featureCode);
////            log.info("特征码参数:{}", afterSaveEntity);
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 售后版本 特征码
//                 */
//                String featureCode = da.getVerifyData();
//                daEntity.setFeatureCode(featureCode);
//                daMapper.insert(daEntity);
//            }
//        } else if (scene.equals(ModelEnum.RESEARCH.getCode())) {
////            String[] data = da.getVerifyData().split(",");
////            authServicel.oaTokenAuth(data[0], data[1]);
//            if (StringUtils.isNotBlank(chipId)) {
//                daMapper.insert(daEntity);
//            }
//        } else if (scene.equals(ModelEnum.FANS.getCode())) {
//            FansEntity fansEntity = authServicel.palmIdAuth(da.getVerifyData());
//            daEntity.setAccountInformation(fansEntity.getUniqueId());
//            if (StringUtils.isNotBlank(chipId)) {
//                daMapper.insert(daEntity);
//            }
//        } else if (scene.equals(ModelEnum.AFTET_SAVE.getCode())) {
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 售后版本 特征码
//                 */
//                String featureCode = da.getVerifyData();
//                daEntity.setFeatureCode(featureCode);
//                daMapper.insert(daEntity);
//            }
//        } else {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
//        byte[] randomNumber = null;
//        try {
//            log.info("开始验签");
//            //验签 Base64转码后的数据
//            boolean verify = RSAUtils.verify(da.getEncodeRandomNumber().getBytes(), authKeyResource.getPcPublicKey(), da.getSign());
//            log.info("验签结果为:{}", verify);
//            if (verify) {
//                log.info("开始解密");
//                randomNumber = RSAUtils.rsaDecryptionOaepSha256ByPrivate(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), Base64Decoder.decode(da.getEncodeRandomNumber()));
//            } else {
//                log.info("验签失败");
//                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
//            }
//            //sha256
//            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//            messageDigest.update(randomNumber);
//            byte[] result = messageDigest.digest();
//            byte[] phoneSign = RSAUtils.signByZr(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), result);
//            String pcSign = RSAUtils.sign(Base64Encoder.encode(phoneSign).getBytes(), authKeyResource.getServerPrivateKey());
//
//            DaZrResp daResp = new DaZrResp();
//            daResp.setPcSign(pcSign);
//            daResp.setPhoneSign(Base64Encoder.encode(phoneSign));
//            daResp.setRandomNumber(Base64Encoder.encode(randomNumber));
//            return daResp;
//        } catch (Exception e) {
//            log.info("失败:{}", e.getMessage());
//            throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
//        }
//    }

    @Override
    public DaResp mtkDownloadVerifyV2(DaReq da) throws Exception {
        String chipId = request.getHeader("Chip-ID");
        String chipPlatform = request.getHeader("Chip-Platform");
        String toolVersion = request.getHeader("Tool-Version");
        String projectVersion = request.getHeader("Project-Version");
        String upgradeModel = request.getHeader("Upgrade-Model");
        String softwareVersion = request.getHeader("Software-Version");
        String deviceModel = request.getHeader("Device-Model");
        String deviceBrand = request.getHeader("Device-Brand");
        String workOrderID = request.getHeader("Work-Order-ID");
        Integer scene = da.getScene();
        String chipOriginalId = request.getHeader("Chip-original-ID");
        String timestamp = request.getHeader("Timestamp");
        String headerSign = request.getHeader("Sign");
//        if (toolVersion.equals("LogCatcher")) {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
        String signStr = chipId + "&" + chipPlatform + "&" + toolVersion + "&" + projectVersion + "&" + upgradeModel + "&" + softwareVersion + "&" + deviceModel + "&" +
                deviceBrand + "&" + workOrderID + "&" + scene + "&" + chipOriginalId + "&" + timestamp + "&";
        signStr = signStr.replace("null", "");
        log.info("signStr:{}", signStr);
        String signHash256 = SHAUtils.getSHA256(signStr);
        // 验签请求头
        boolean headerVerify = RSAUtils.verify(signHash256.getBytes(), authKeyResource.getPcPublicKey(), headerSign);
        if (!headerVerify) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
        DaEntity daEntity = new DaEntity();
        daEntity.setChipId(chipId);
        daEntity.setChipOriginalId(chipOriginalId);
        daEntity.setChipPlatform(chipPlatform);
        daEntity.setToolVersion(toolVersion);
        daEntity.setCreateTime(new Date());
        daEntity.setProjectVersion(projectVersion);
        daEntity.setToolType(upgradeModel);
        daEntity.setSoftwareVersion(softwareVersion);
        daEntity.setDeviceModel(deviceModel);
        daEntity.setDeviceBrand(deviceBrand);
        daEntity.setWorkOrderID(workOrderID);
        log.info("chipId: {} header verify success,data:{}", chipId, JSON.toJSONString(daEntity));
        //场景验证
        daEntity.setScene(scene);
        if (scene.equals(ModelEnum.FACTORY.getCode())) {
            if (StringUtils.isNotBlank(chipId)) {
                /**
                 * 售后版本 特征码
                 */
                String featureCode = da.getVerifyData();
                daEntity.setFeatureCode(featureCode);
                daMapper.insert(daEntity);
            }
            //            log.info("特征码校验开始:{}", featureCode);
//            AfterSaveEntity afterSaveEntity = authServicel.featureCodeAuth(featureCode);
//            log.info("特征码参数:{}", afterSaveEntity);
        } else if (scene.equals(ModelEnum.RESEARCH.getCode())) {
            if (StringUtils.isNotBlank(chipId)) {
                daMapper.insert(daEntity);
            }
            String[] data = da.getVerifyData().split(",");
            authServicel.oaTokenAuth(data[0], data[1]);
        } else if (scene.equals(ModelEnum.FANS.getCode())) {
            if (StringUtils.isNotBlank(chipId)) {
                daMapper.insert(daEntity);
            }
            FansEntity fansEntity = authServicel.palmIdAuth(da.getVerifyData());
            daEntity.setAccountInformation(fansEntity.getUniqueId());
        } else if (scene.equals(ModelEnum.AFTET_SAVE.getCode())) {
            String featureCode = da.getVerifyData();
            if (StringUtils.isNotBlank(chipId)) {
                /**
                 * 售后版本 特征码
                 */
                daEntity.setFeatureCode(featureCode);
                daMapper.insert(daEntity);
            }
            authServicel.featureCodeAuth(featureCode);
        } else if (scene.equals(ModelEnum.LOGCATCHER.getCode())) {
            String featureCode = da.getVerifyData();
            if (StringUtils.isNotBlank(chipId)) {
                /**
                 * 售后版本 特征码
                 */
                daEntity.setFeatureCode(featureCode);
                daMapper.insert(daEntity);
            }
            authServicel.featureCodeAuth(featureCode);
        } else {
            throw new CustomException(NetCodeEnum.SCENE_FAIL);
        }

        //数据验证
        String randomNumber = "";
        try {
            //验签 Base64转码后的数据
            boolean verify = RSAUtils.verify(da.getEncodeRandomNumber().getBytes(), authKeyResource.getPcPublicKey(), da.getSign());
            if (verify) {
                randomNumber = RSAUtils.decryptByPrivateKey(authKeyResource.getServerPrivateKey(), da.getEncodeRandomNumber(), RSAUtils.RSA_LENGTH_2048);
                if (StringUtils.isBlank(randomNumber)) {
                    throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
                }
            } else {
                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
            }
            byte[] encodePhone = RSAUtils.rsaEncryptionOaepSha256ByPrivate(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), Base64Decoder.decode(randomNumber));
            String sign = RSAUtils.sign(Base64Encoder.encode(encodePhone).getBytes(), authKeyResource.getServerPrivateKey());
            DaResp daResp = new DaResp();
            daResp.setSign(sign);
            daResp.setEncodePc(Base64Encoder.encode(encodePhone));
            return daResp;
        } catch (Exception e) {
            log.info("chipId:{} error:{}", chipId, e.getMessage());
            throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
        }
    }

    @Override
    public DaZrResp sprdFdlVerifyV2(DaReq da) throws Exception {
        String chipId = request.getHeader("Chip-ID");
        String chipPlatform = request.getHeader("Chip-Platform");
        String toolVersion = request.getHeader("Tool-Version");
        String projectVersion = request.getHeader("Project-Version");
        String upgradeModel = request.getHeader("Upgrade-Model");
        String softwareVersion = request.getHeader("Software-Version");
        String deviceModel = request.getHeader("Device-Model");
        String deviceBrand = request.getHeader("Device-Brand");
        String workOrderID = request.getHeader("Work-Order-ID");
        Integer scene = da.getScene();
        String chipOriginalId = request.getHeader("Chip-original-ID");
        String timestamp = request.getHeader("Timestamp");
        String headerSign = request.getHeader("Sign");
//        if (toolVersion.equals("LogCatcher")) {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
        String signStr = chipId + "&" + chipPlatform + "&" + toolVersion + "&" + projectVersion + "&" + upgradeModel + "&" + softwareVersion + "&" + deviceModel + "&" +
                deviceBrand + "&" + workOrderID + "&" + scene + "&" + chipOriginalId + "&" + timestamp + "&";
        signStr = signStr.replace("null", "");
        String signHash256 = SHAUtils.getSHA256(signStr);
        log.info("signStr:{}", signStr);
        // 验签请求头
        boolean headerVerify = RSAUtils.verify(signHash256.getBytes(), authKeyResource.getPcPublicKey(), headerSign);
        if (!headerVerify) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
        DaEntity daEntity = new DaEntity();
        daEntity.setChipId(chipId);
        daEntity.setChipOriginalId(chipOriginalId);
        daEntity.setChipPlatform(chipPlatform);
        daEntity.setToolVersion(toolVersion);
        daEntity.setCreateTime(new Date());
        daEntity.setProjectVersion(projectVersion);
        daEntity.setToolType(upgradeModel);
        daEntity.setSoftwareVersion(softwareVersion);
        daEntity.setDeviceModel(deviceModel);
        daEntity.setDeviceBrand(deviceBrand);
        daEntity.setWorkOrderID(workOrderID);
        log.info("chipId: {} header verify success,data:{}", chipId, JSON.toJSONString(daEntity));
        //场景验证
        daEntity.setScene(scene);
        if (scene.equals(ModelEnum.FACTORY.getCode())) {
//            log.info("特征码校验开始:{}", featureCode);
//            AfterSaveEntity afterSaveEntity = authServicel.featureCodeAuth(featureCode);
//            log.info("特征码参数:{}", afterSaveEntity);
            if (StringUtils.isNotBlank(chipId)) {
                /**
                 * 售后版本 特征码
                 */
                String featureCode = da.getVerifyData();
                daEntity.setFeatureCode(featureCode);
                daMapper.insert(daEntity);
            }
        } else if (scene.equals(ModelEnum.RESEARCH.getCode())) {
            if (StringUtils.isNotBlank(chipId)) {
                daMapper.insert(daEntity);
            }
            String[] data = da.getVerifyData().split(",");
            authServicel.oaTokenAuth(data[0], data[1]);
        } else if (scene.equals(ModelEnum.FANS.getCode())) {
            if (StringUtils.isNotBlank(chipId)) {
                daMapper.insert(daEntity);
            }
            FansEntity fansEntity = authServicel.palmIdAuth(da.getVerifyData());
            daEntity.setAccountInformation(fansEntity.getUniqueId());
        } else if (scene.equals(ModelEnum.AFTET_SAVE.getCode())) {
            String featureCode = da.getVerifyData();
            if (StringUtils.isNotBlank(chipId)) {
                /**
                 * 售后版本 特征码
                 */
                daEntity.setFeatureCode(featureCode);
                daMapper.insert(daEntity);
            }
            authServicel.featureCodeAuth(featureCode);
        } else if (scene.equals(ModelEnum.LOGCATCHER.getCode())) {
            String featureCode = da.getVerifyData();
            if (StringUtils.isNotBlank(chipId)) {
                /**
                 * 售后版本 特征码
                 */
                daEntity.setFeatureCode(featureCode);
                daMapper.insert(daEntity);
            }
            authServicel.featureCodeAuth(featureCode);
        } else {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        byte[] randomNumber = null;
        try {
            //验签 Base64转码后的数据
            boolean verify = RSAUtils.verify(da.getEncodeRandomNumber().getBytes(), authKeyResource.getPcPublicKey(), da.getSign());
            if (verify) {
                randomNumber = RSAUtils.rsaDecryptionOaepSha256ByPrivate(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), Base64Decoder.decode(da.getEncodeRandomNumber()));
            } else {
                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
            }
            //sha256
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(randomNumber);
            byte[] result = messageDigest.digest();
            byte[] phoneSign = RSAUtils.signByZr(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), result);
            String pcSign = RSAUtils.sign(Base64Encoder.encode(phoneSign).getBytes(), authKeyResource.getServerPrivateKey());

            DaZrResp daResp = new DaZrResp();
            daResp.setPcSign(pcSign);
            daResp.setPhoneSign(Base64Encoder.encode(phoneSign));
            daResp.setRandomNumber(Base64Encoder.encode(randomNumber));
            return daResp;
        } catch (Exception e) {
            log.info("chipId:{} error:{}", chipId, e.getMessage());
            throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
        }
    }

    @Override
    public DaResp mtkDownloadVerifyV3(DaReq da) throws Exception {
        String chipId = request.getHeader("Chip-ID");
        String chipPlatform = request.getHeader("Chip-Platform");
        String toolVersion = request.getHeader("Tool-Version");
        String projectVersion = request.getHeader("Project-Version");
        String upgradeModel = request.getHeader("Upgrade-Model");
        String softwareVersion = request.getHeader("Software-Version");
        String deviceModel = request.getHeader("Device-Model");
        String deviceBrand = request.getHeader("Device-Brand");
        String workOrderID = request.getHeader("Work-Order-ID");
        Integer scene = da.getScene();
        String chipOriginalId = request.getHeader("Chip-original-ID");
        String timestamp = request.getHeader("Timestamp");
        String headerSign = request.getHeader("Sign");
//        if (toolVersion.equals("LogCatcher")) {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
        if (System.currentTimeMillis() - Long.parseLong(timestamp) * 1000L > 1000 * 60 * 60 * 24) {
            throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
        }
        String signStr = chipId + "&" + chipPlatform + "&" + toolVersion + "&" + projectVersion + "&" + upgradeModel + "&" + softwareVersion + "&" + deviceModel + "&" +
                deviceBrand + "&" + workOrderID + "&" + scene + "&" + chipOriginalId + "&" + timestamp + "&";
        signStr = signStr.replace("null", "");
        log.info("signStr:{}", signStr);
        String signHash256 = SHAUtils.getSHA256(signStr);
        log.info("hash(sign):{}", signHash256);
        // 验签请求头
        boolean headerVerify = RSAUtils.verify(signHash256.getBytes(), authKeyResource.getPcPublicKeyV3(), headerSign);
        if (!headerVerify) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
//        DaEntity daEntity = new DaEntity();
//        daEntity.setChipId(chipId);
//        daEntity.setChipOriginalId(chipOriginalId);
//        daEntity.setChipPlatform(chipPlatform);
//        daEntity.setToolVersion(toolVersion);
//        daEntity.setCreateTime(new Date());
//        daEntity.setProjectVersion(projectVersion);
//        daEntity.setToolType(upgradeModel);
//        daEntity.setSoftwareVersion(softwareVersion);
//        daEntity.setDeviceModel(deviceModel);
//        daEntity.setDeviceBrand(deviceBrand);
//        daEntity.setWorkOrderID(workOrderID);
//
//        //场景验证
//        daEntity.setScene(scene);
        if (scene.equals(ModelEnum.FACTORY.getCode())) {
            String featureCode = da.getVerifyData();
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 售后版本 特征码
//                 */
//                daEntity.setFeatureCode(featureCode);
//                daEntity.setScene(4);
//                daMapper.insert(daEntity);
//            }
            AfterSaveEntity afterSaveEntity = authServicel.featureCodeAuth(featureCode);
        } else if (scene.equals(ModelEnum.RESEARCH.getCode())) {
//            if (StringUtils.isNotBlank(chipId)) {
//                daMapper.insert(daEntity);
//            }
            String[] data = da.getVerifyData().split(",");
            authServicel.oaTokenAuth(data[0], data[1]);
        } else if (scene.equals(ModelEnum.FANS.getCode())) {
//            if (StringUtils.isNotBlank(chipId)) {
//                daMapper.insert(daEntity);
//            }
            FansEntity fansEntity = authServicel.palmIdAuth(da.getVerifyData());
//            daEntity.setAccountInformation(fansEntity.getUniqueId());
        } else if (scene.equals(ModelEnum.AFTET_SAVE.getCode())) {
            String featureCode = da.getVerifyData();
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 售后版本 特征码
//                 */
//                daEntity.setFeatureCode(featureCode);
//                daMapper.insert(daEntity);
//            }
            authServicel.featureCodeAuth(featureCode);
        } else if (scene.equals(ModelEnum.LOGCATCHER.getCode())) {
            String featureCode = da.getVerifyData();
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 售后版本 特征码
//                 */
//                daEntity.setFeatureCode(featureCode);
//                daMapper.insert(daEntity);
//            }
            authServicel.featureCodeAuth(featureCode);
        } else {
            throw new CustomException(NetCodeEnum.SCENE_FAIL);
        }

        //数据验证
        String randomNumber = "";
        try {
            //验签 Base64转码后的数据
            boolean verify = RSAUtils.verify(da.getEncodeRandomNumber().getBytes(), authKeyResource.getPcPublicKeyV3(), da.getSign());
            if (verify) {
                randomNumber = RSAUtils.decryptByPrivateKey(authKeyResource.getServerPrivateKeyV3(), da.getEncodeRandomNumber(), RSAUtils.RSA_LENGTH_2048);
                if (StringUtils.isBlank(randomNumber)) {
                    throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
                }
            } else {
                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
            }
            byte[] encodePhone = RSAUtils.rsaEncryptionOaepSha256ByPrivate(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), Base64Decoder.decode(randomNumber));
            String sign = RSAUtils.sign(Base64Encoder.encode(encodePhone).getBytes(), authKeyResource.getServerPrivateKeyV3());

            DaResp daResp = new DaResp();
            daResp.setSign(sign);
            daResp.setEncodePc(Base64Encoder.encode(encodePhone));
            return daResp;
        } catch (Exception e) {
            log.info("error:{}", e.getMessage());
            throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
        }
    }

    @Override
    public DaZrResp sprdFdlVerifyV3(DaReq da) throws Exception {
        String chipId = request.getHeader("Chip-ID");
        String chipPlatform = request.getHeader("Chip-Platform");
        String toolVersion = request.getHeader("Tool-Version");
        String projectVersion = request.getHeader("Project-Version");
        String upgradeModel = request.getHeader("Upgrade-Model");
        String softwareVersion = request.getHeader("Software-Version");
        String deviceModel = request.getHeader("Device-Model");
        String deviceBrand = request.getHeader("Device-Brand");
        String workOrderID = request.getHeader("Work-Order-ID");
        Integer scene = da.getScene();
        String chipOriginalId = request.getHeader("Chip-original-ID");
        String timestamp = request.getHeader("Timestamp");
        String headerSign = request.getHeader("Sign");
//        if (toolVersion.equals("LogCatcher")) {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
        if (System.currentTimeMillis() - Long.parseLong(timestamp) * 1000L > 1000 * 60 * 60 * 24) {
            throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
        }
        String signStr = chipId + "&" + chipPlatform + "&" + toolVersion + "&" + projectVersion + "&" + upgradeModel + "&" + softwareVersion + "&" + deviceModel + "&" +
                deviceBrand + "&" + workOrderID + "&" + scene + "&" + chipOriginalId + "&" + timestamp + "&";
        signStr = signStr.replace("null", "");
        String signHash256 = SHAUtils.getSHA256(signStr);
        log.info("data:{}", signStr);
        log.info("hash(sign):{}", signHash256);
        // 验签请求头
        boolean headerVerify = RSAUtils.verify(signHash256.getBytes(), authKeyResource.getPcPublicKeyV3(), headerSign);
        if (!headerVerify) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
//        DaEntity daEntity = new DaEntity();
//        daEntity.setChipId(chipId);
//        daEntity.setChipOriginalId(chipOriginalId);
//        daEntity.setChipPlatform(chipPlatform);
//        daEntity.setToolVersion(toolVersion);
//        daEntity.setCreateTime(new Date());
//        daEntity.setProjectVersion(projectVersion);
//        daEntity.setToolType(upgradeModel);
//        daEntity.setSoftwareVersion(softwareVersion);
//        daEntity.setDeviceModel(deviceModel);
//        daEntity.setDeviceBrand(deviceBrand);
//        daEntity.setWorkOrderID(workOrderID);
        //场景验证
//        daEntity.setScene(scene);
        if (scene.equals(ModelEnum.FACTORY.getCode())) {
            String featureCode = da.getVerifyData();
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 工厂 特征码
//                 */
//                daEntity.setScene(4);
//                daEntity.setFeatureCode(featureCode);
//                daMapper.insert(daEntity);
//            }
            AfterSaveEntity afterSaveEntity = authServicel.featureCodeAuth(featureCode);
        } else if (scene.equals(ModelEnum.RESEARCH.getCode())) {
//            if (StringUtils.isNotBlank(chipId)) {
//                daMapper.insert(daEntity);
//            }
            String[] data = da.getVerifyData().split(",");
            authServicel.oaTokenAuth(data[0], data[1]);
        } else if (scene.equals(ModelEnum.FANS.getCode())) {
//            if (StringUtils.isNotBlank(chipId)) {
//                daMapper.insert(daEntity);
//            }
            FansEntity fansEntity = authServicel.palmIdAuth(da.getVerifyData());
//            daEntity.setAccountInformation(fansEntity.getUniqueId());
        } else if (scene.equals(ModelEnum.AFTET_SAVE.getCode())) {
            String featureCode = da.getVerifyData();
//            if (StringUtils.isNotBlank(chipId)) {
//                /**
//                 * 售后版本 特征码
//                 */
//                daEntity.setFeatureCode(featureCode);
//                daMapper.insert(daEntity);
//            }
            authServicel.featureCodeAuth(featureCode);
        } else {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        byte[] randomNumber = null;
        try {
            //验签 Base64转码后的数据
            boolean verify = RSAUtils.verify(da.getEncodeRandomNumber().getBytes(), authKeyResource.getPcPublicKeyV3(), da.getSign());
            if (verify) {
                randomNumber = RSAUtils.rsaDecryptionOaepSha256ByPrivate(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), Base64Decoder.decode(da.getEncodeRandomNumber()));
            } else {
                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
            }
            //sha256
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(randomNumber);
            byte[] result = messageDigest.digest();
            byte[] phoneSign = RSAUtils.signByZr(RsaHelper.generatePrivateRSAKey(authKeyResource.getPhonePrivateKey()), result);
            String pcSign = RSAUtils.sign(Base64Encoder.encode(phoneSign).getBytes(), authKeyResource.getServerPrivateKeyV3());

            DaZrResp daResp = new DaZrResp();
            daResp.setPcSign(pcSign);
            daResp.setPhoneSign(Base64Encoder.encode(phoneSign));
            daResp.setRandomNumber(Base64Encoder.encode(randomNumber));
            return daResp;
        } catch (Exception e) {
            log.info("error:{}", e.getMessage());
            throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
        }
    }

    @Override
    public Boolean mtkDownloadRecord(AuthRecordReq da) throws Exception {
        String chipId = request.getHeader("Chip-ID");
        String chipPlatform = request.getHeader("Chip-Platform");
        String toolVersion = request.getHeader("Tool-Version");
        String projectVersion = request.getHeader("Project-Version");
        String upgradeModel = request.getHeader("Upgrade-Model");
        String softwareVersion = request.getHeader("Software-Version");
        String deviceModel = request.getHeader("Device-Model");
        String deviceBrand = request.getHeader("Device-Brand");
        String workOrderID = request.getHeader("Work-Order-ID");
        Integer scene = da.getScene();
        String chipOriginalId = request.getHeader("Chip-original-ID");
        String timestamp = request.getHeader("Timestamp");
        String headerSign = request.getHeader("Sign");
//        if (toolVersion.equals("LogCatcher")) {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
        if (System.currentTimeMillis() - Long.parseLong(timestamp) * 1000L > 1000 * 60 * 60 * 24) {
            throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
        }
        String signStr = chipId + "&" + chipPlatform + "&" + toolVersion + "&" + projectVersion + "&" + upgradeModel + "&" + softwareVersion + "&" + deviceModel + "&" +
                deviceBrand + "&" + workOrderID + "&" + scene + "&" + chipOriginalId + "&" + timestamp + "&";
        signStr = signStr.replace("null", "");
        log.info("signStr:{}", signStr);
        String signHash256 = SHAUtils.getSHA256(signStr);
        log.info("hash(sign):{}", signHash256);
        // 验签请求头
        boolean headerVerify = RSAUtils.verify(signHash256.getBytes(), authKeyResource.getPcPublicKeyV3(), headerSign);
        if (!headerVerify) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
        DaEntity daEntity = new DaEntity();
        daEntity.setChipId(chipId);
        daEntity.setChipOriginalId(chipOriginalId);
        daEntity.setChipPlatform(chipPlatform);
        daEntity.setToolVersion(toolVersion);
        daEntity.setCreateTime(new Date());
        daEntity.setProjectVersion(projectVersion);
        daEntity.setToolType(upgradeModel);
        daEntity.setSoftwareVersion(softwareVersion);
        daEntity.setDeviceModel(deviceModel);
        daEntity.setDeviceBrand(deviceBrand);
        daEntity.setWorkOrderID(workOrderID);

        //场景验证
        daEntity.setScene(scene);
        if (scene.equals(ModelEnum.FACTORY.getCode())) {
            String featureCode = da.getVerifyData();
            /**
             * 售后版本 特征码
             */
            daEntity.setFeatureCode(featureCode);
            daMapper.insert(daEntity);
        } else if (scene.equals(ModelEnum.RESEARCH.getCode())) {
            daMapper.insert(daEntity);
        } else if (scene.equals(ModelEnum.FANS.getCode())) {
            daMapper.insert(daEntity);
        } else if (scene.equals(ModelEnum.AFTET_SAVE.getCode())) {
            String featureCode = da.getVerifyData();
            /**
             * 售后版本 特征码
             */
            daEntity.setFeatureCode(featureCode);
            daMapper.insert(daEntity);
        } else {
            throw new CustomException(NetCodeEnum.SCENE_FAIL);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean sprdFdlRecord(AuthRecordReq da) throws Exception {
        String chipId = request.getHeader("Chip-ID");
        String chipPlatform = request.getHeader("Chip-Platform");
        String toolVersion = request.getHeader("Tool-Version");
        String projectVersion = request.getHeader("Project-Version");
        String upgradeModel = request.getHeader("Upgrade-Model");
        String softwareVersion = request.getHeader("Software-Version");
        String deviceModel = request.getHeader("Device-Model");
        String deviceBrand = request.getHeader("Device-Brand");
        String workOrderID = request.getHeader("Work-Order-ID");
        Integer scene = da.getScene();
        String chipOriginalId = request.getHeader("Chip-original-ID");
        String timestamp = request.getHeader("Timestamp");
        String headerSign = request.getHeader("Sign");
//        if (toolVersion.equals("LogCatcher")) {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
        if (System.currentTimeMillis() - Long.parseLong(timestamp) * 1000L > 1000 * 60 * 60 * 24) {
            throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
        }
        String signStr = chipId + "&" + chipPlatform + "&" + toolVersion + "&" + projectVersion + "&" + upgradeModel + "&" + softwareVersion + "&" + deviceModel + "&" +
                deviceBrand + "&" + workOrderID + "&" + scene + "&" + chipOriginalId + "&" + timestamp + "&";
        signStr = signStr.replace("null", "");
        String signHash256 = SHAUtils.getSHA256(signStr);
        log.info("data:{}", signStr);
        log.info("hash(sign):{}", signHash256);
        // 验签请求头
        boolean headerVerify = RSAUtils.verify(signHash256.getBytes(), authKeyResource.getPcPublicKeyV3(), headerSign);
        if (!headerVerify) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
        DaEntity daEntity = new DaEntity();
        daEntity.setChipId(chipId);
        daEntity.setChipOriginalId(chipOriginalId);
        daEntity.setChipPlatform(chipPlatform);
        daEntity.setToolVersion(toolVersion);
        daEntity.setCreateTime(new Date());
        daEntity.setProjectVersion(projectVersion);
        daEntity.setToolType(upgradeModel);
        daEntity.setSoftwareVersion(softwareVersion);
        daEntity.setDeviceModel(deviceModel);
        daEntity.setDeviceBrand(deviceBrand);
        daEntity.setWorkOrderID(workOrderID);
        //场景验证
        daEntity.setScene(scene);
        if (scene.equals(ModelEnum.FACTORY.getCode())) {
            String featureCode = da.getVerifyData();
            /**
             * 工厂 特征码
             */
            daEntity.setFeatureCode(featureCode);
            daMapper.insert(daEntity);
        } else if (scene.equals(ModelEnum.RESEARCH.getCode())) {
            daMapper.insert(daEntity);
        } else if (scene.equals(ModelEnum.FANS.getCode())) {
            daMapper.insert(daEntity);
        } else if (scene.equals(ModelEnum.AFTET_SAVE.getCode())) {
            String featureCode = da.getVerifyData();
            /**
             * 售后版本 特征码
             */
            daEntity.setFeatureCode(featureCode);
            daMapper.insert(daEntity);
        } else {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        return Boolean.TRUE;
    }

}
