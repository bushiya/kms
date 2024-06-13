package com.transsion.authentication.module.da.service.impl;

import com.alibaba.fastjson.JSON;
import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.utils.RSAUtils;
import com.transsion.authentication.module.da.repository.entity.AfterSaveEntity;
import com.transsion.authentication.module.da.repository.entity.FansEntity;
import com.transsion.authentication.module.da.repository.resource.AuthResource;
import com.transsion.authentication.module.da.service.AuthService;
import com.transsion.authentication.module.da.service.bean.req.AfterFeatureCodeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;

/**
 * @Description:
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

    public static void main(String[] args) {
        System.out.println(String.valueOf(new Date().getTime()).substring(0, 7));
    }

    @Override
    public AfterSaveEntity featureCodeAuth(String featureCode) {
        try {
            String timeCode = String.valueOf(new Date().getTime()).substring(0, 7);
            String verifyCode = RSAUtils.encryptByPublicKey(authResource.getPublicKey(), featureCode + "#*#s#jkEeWw" + timeCode, 1024);
            // 构建你的请求头
            AfterFeatureCodeReq req = new AfterFeatureCodeReq();
            req.setNewFeatureCode(featureCode);
            req.setVerifyCode(verifyCode);
            HttpHeaders headers = new HttpHeaders();
            // 组合请求头与请求体参数
            HttpEntity<String> requestEntity = new HttpEntity(req, headers);
            String s = restTemplate.postForObject(authResource.getAfterSaveUrl(), requestEntity, String.class);
            HashMap<String, Object> response = JSON.parseObject(s, HashMap.class);
            if (response.get("code").equals("0000")) {
                Object data = response.get("data");
                if (ObjectUtils.isEmpty(data)) {
                    throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
                }
                AfterSaveEntity afterSaveEntity = JSON.parseObject(data.toString(), AfterSaveEntity.class);
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
            String s = restTemplate.postForObject(authResource.getResearchUrl(), requestEntity, String.class);
            CommResponse<Boolean> response = JSON.parseObject(s, CommResponse.class);
            if (response.getCode() == 200 && response.getData()) {
                return Boolean.TRUE;
            } else {
                throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
            }
        } catch (Exception e) {
            log.info("OA账号接口错误：{}", e.getMessage());
            throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
        }
    }

    @Override
    public FansEntity palmIdAuth(String token) {
        try {
            // 构建你的请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Client-ID", "da-tool-auth");
            // 组合请求头与请求体参数
            HttpEntity<String> requestEntity = new HttpEntity(headers);
            ResponseEntity<String> s = restTemplate.exchange(authResource.getFansUrl(), HttpMethod.GET, requestEntity, String.class);
            FansEntity response = JSON.parseObject(s.getBody(), FansEntity.class);
            if (s.getStatusCodeValue() == 200 && !ObjectUtils.isEmpty(response)) {
                return response;
            } else {
                throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
            }
        } catch (Exception e) {
            log.info("账号校验接口错误：{}", e.getMessage());
            throw new CustomException(NetCodeEnum.IDENTITY_FAIL);
        }
    }
}

