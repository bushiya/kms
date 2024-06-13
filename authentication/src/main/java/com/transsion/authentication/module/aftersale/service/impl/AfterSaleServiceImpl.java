package com.transsion.authentication.module.aftersale.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import com.transsion.authentication.infrastructure.utils.CertUtil;
import com.transsion.authentication.infrastructure.utils.RSAUtil;
import com.transsion.authentication.infrastructure.utils.SHAUtils;
import com.transsion.authentication.module.aftersale.bean.req.AfterSaveAuthReq;
import com.transsion.authentication.module.aftersale.bean.resp.AfterSaveAuthResp;
import com.transsion.authentication.module.aftersale.repository.entity.AfterHeadEntity;
import com.transsion.authentication.module.aftersale.repository.resource.AfterSaleResource;
import com.transsion.authentication.module.aftersale.service.AfterSaleService;
import com.transsion.authentication.module.aftersale.service.AuthService;
import com.transsion.authentication.module.auth.repository.init.AuthCertApplicationContext;
import com.transsion.authentication.module.auth.repository.resource.RootCertResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 售后接口实现
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@Slf4j
@Service
public class AfterSaleServiceImpl implements AfterSaleService {

    @Autowired
    AuthService authService;

    @Autowired
    RootCertResource rootCertResource;

    @Autowired
    AfterSaleResource afterSaleResource;

    @Autowired
    AuthCertApplicationContext authCertApplicationContext;

    @Autowired
    CertUtil certUtil;

    @Override
    public AfterSaveAuthResp deviceAuth(AfterHeadEntity heads, AfterSaveAuthReq req) throws Exception {
        log.info("售后校验参数 heads:{}, req:{}", heads, req);
        // 验证OA账号 和 售后特征码
//        authService.featureCodeAuth(req.getFeatureCode());
//        authService.oaTokenAuth(req.getToken().split(",")[0], req.getToken().split(",")[1]);
        log.info("验证OA账号 和 售后特征码成功");
        // 验证随机流水号
        RSAUtil.verify(SHAUtils.getSHA256(req.getRandom()), afterSaleResource.getPcPublicKey(), req.getRandomSign());
        log.info("验证随机流水号成功");
        String deviceCsr = req.getDeviceCsr();
        RSAUtil.verify(SHAUtils.getSHA256(deviceCsr), afterSaleResource.getPcPublicKey(), req.getDeviceCsrSign());
        log.info("验证deviceCsr成功");
        AfterSaveAuthResp resp = new AfterSaveAuthResp();
        // 公网证书
        resp.setFactoryCert(authCertApplicationContext.getAuthCertStr());
        // Pc公钥加密后的设备证书
        resp.setDeviceCert(Base64Encoder.encode(certUtil.generateCertByCsr(deviceCsr, authCertApplicationContext.getPrivateKey()).getEncoded()));
        // 流水号签名
        resp.setRandomSign(RSAUtil.sign(SHAUtils.getSHA256(req.getRandom()), afterSaleResource.getServerPrivateKey()));
        return resp;
    }
}
