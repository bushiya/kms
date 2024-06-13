package com.transsion.authentication.module.auth.service.impl;

import com.transsion.authentication.infrastructure.annotation.ServerPort;
import com.transsion.authentication.infrastructure.utils.AESUtil;
import com.transsion.authentication.infrastructure.utils.RSAUtil;
import com.transsion.authentication.infrastructure.utils.RandomUtil;
import com.transsion.authentication.module.auth.bean.req.ServerInitReq;
import com.transsion.authentication.module.auth.bean.resp.ServerInitResp;
import com.transsion.authentication.module.auth.repository.dao.ServerCommunicationDao;
import com.transsion.authentication.module.auth.repository.entity.ServerCommunicationEntity;
import com.transsion.authentication.module.auth.repository.init.AuthStorageKeyApplicationContext;
import com.transsion.authentication.module.auth.repository.init.DeviceAppContext;
import com.transsion.authentication.module.auth.service.ServerService;
import com.transsion.authentication.module.sys.repository.entity.DeviceAppEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/11
 */
@Service
public class ServerServiceImpl implements ServerService {

    @Autowired
    AuthStorageKeyApplicationContext context;

    @Autowired
    DeviceAppContext deviceAppContext;

    @Autowired
    ServerCommunicationDao serverCommunicationDao;


    @Override
    public String selectKey(String appId, String serverTag) throws Exception {
        ServerCommunicationEntity serverCommunicationEntity = serverCommunicationDao.selectByAppIdAndServerTag(appId, serverTag);
        return AESUtil.decrypt(serverCommunicationEntity.getSecretKey(), context.getStorageKey());
    }

    @Override
    @ServerPort
    public ServerInitResp init(String appId, ServerInitReq req) throws Exception {
        DeviceAppEntity deviceApp = deviceAppContext.getDeviceApp(appId);
        String randomNumber = req.getRandomNumber();
        RSAUtil.verify(randomNumber, deviceApp.getAppPub(), req.getVerifyMessage());
        // 业务方服务器通信密钥
        String secretKey = AESUtil.encrypt(randomNumber, RandomUtil.getRandomNumber(16)).substring(0, 16);

        ServerCommunicationEntity data = new ServerCommunicationEntity();
        data.setAppId(appId);
        data.setServerTag(randomNumber);
        data.setSecretKey(AESUtil.encrypt(secretKey, context.getStorageKey()));
        data.setCreateTime(new Date());
        data.setUpdateTime(new Date());
        serverCommunicationDao.save(data);

        ServerInitResp resp = new ServerInitResp();
        resp.setSign(RSAUtil.sign(secretKey, deviceApp.getServerPrivateKey()));
        resp.setSecretKey(RSAUtil.encryptByPublicKey(secretKey, deviceApp.getAppPub()));
        return resp;
    }
}
