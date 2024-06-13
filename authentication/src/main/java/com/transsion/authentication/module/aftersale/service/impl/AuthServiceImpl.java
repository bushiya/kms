package com.transsion.authentication.module.aftersale.service.impl;

import com.alibaba.fastjson.JSON;
import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.utils.AfterSaveUtils;
import com.transsion.authentication.module.aftersale.repository.entity.AfterSaveEntity;
import com.transsion.authentication.module.aftersale.repository.resource.AuthResource;
import com.transsion.authentication.module.aftersale.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @Description: 售后认证 第三方校验接口
 * @Author jiakang.chen
 * @Date 2023/6/10
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AuthResource authResource;

    @Override
    public AfterSaveEntity featureCodeAuth(String featureCode) {
        try {
            HashMap<String, String> param = new HashMap<>();
            param.put("featureCode", featureCode);
            // 构建你的请求头
            HttpHeaders headers = new HttpHeaders();
            // 组合请求头与请求体参数
            HttpEntity<String> requestEntity = new HttpEntity(AfterSaveUtils.encrypt(JSON.toJSONString(param)), headers);
            //获取IMEI对应的ChipId
            String s = restTemplate.postForObject(authResource.getAfterSaveUrl(), requestEntity, String.class);

            CommResponse response = JSON.parseObject(AfterSaveUtils.decode(s), CommResponse.class);
            if (response.getCode() == 200) {
                AfterSaveEntity afterSaveEntity = JSON.parseObject(response.getData().toString(), AfterSaveEntity.class);
                //如果为null,返回null
                if (ObjectUtils.isEmpty(afterSaveEntity)) {
                    throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
                } else {
                    return afterSaveEntity;
                }
            } else {
                throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
            }
        } catch (Exception e) {
            log.info("售后校验特征码接口错误：{}", e.getMessage());
            throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
        }
    }

    @Override
    public Boolean oaTokenAuth(String rToken, String uToken) {
        try {
            HashMap<String, String> param = new HashMap<>();
            param.put("rtoken", rToken);
            param.put("utoken", uToken);
            param.put("appId", authResource.getResearchAppId());
            // 构建你的请求头
            HttpHeaders headers = new HttpHeaders();
            // 组合请求头与请求体参数
            HttpEntity<String> requestEntity = new HttpEntity(param, headers);
            //获取IMEI对应的ChipId
            String s = restTemplate.postForObject(authResource.getResearchUrl(), requestEntity, String.class);

            CommResponse<Boolean> response = JSON.parseObject(s, CommResponse.class);
            if (response.getCode() == 200 && response.getData()) {
                return Boolean.TRUE;
            } else {
                throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
            }
        } catch (Exception e) {
            log.info("售后校验特征码接口错误：{}", e.getMessage());
            throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
        }
    }

}
