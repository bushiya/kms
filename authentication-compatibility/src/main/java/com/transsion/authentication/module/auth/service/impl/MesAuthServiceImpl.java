package com.transsion.authentication.module.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.utils.EncryptionUtil;
import com.transsion.authentication.module.auth.repository.dao.ServerCertRecordDao;
import com.transsion.authentication.module.auth.repository.entity.ServerCertRecordEntity;
import com.transsion.authentication.module.auth.repository.entity.ServerStorageKey;
import com.transsion.authentication.module.auth.repository.resource.MesServerSource;
import com.transsion.authentication.module.auth.service.MesAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Slf4j
@Service
public class MesAuthServiceImpl implements MesAuthService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MesServerSource mesServerSource;

    @Autowired
    ServerCertRecordDao serverCertRecordDao;

    @Autowired
    EncryptionUtil encryptionUtil;

    @Override
    public String getAuthCert(String csr) {
        try {
            HashMap<String, String> param = new HashMap<>();
            String authCsr = encryptionUtil.encrypt(csr);
            param.put("authCsr", authCsr);
            param.put("authCsrSign", encryptionUtil.sign(csr));
            // 构建你的请求头
            HttpHeaders headers = new HttpHeaders();
            // 组合请求头与请求体参数
            HttpEntity<String> requestEntity = new HttpEntity(param, headers);
            //获取IMEI对应的ChipId
            String result = restTemplate.postForObject(mesServerSource.getGetCertUrl(), requestEntity, String.class);
            CommResponse commResponse = JSON.parseObject(result, CommResponse.class);
            if (commResponse.getCode() == 200) {
                ServerCertRecordEntity data = JSON.parseObject(commResponse.getData().toString(), ServerCertRecordEntity.class);
                data.setServerTag(2);
                data.setCreateTime(new Date());
                data.setUpdateTime(new Date());
                serverCertRecordDao.insert(data);
                return data.getCert();
            } else {
                throw new CustomException(commResponse.getMessage());
            }
        } catch (Exception e) {
            log.info("MES服务异常：{}", e.getMessage());
            throw new CustomException(NetCodeEnum.MES_ERROR);
        }
    }

    @Override
    public String getStorageKey(String randomNumber) {
        try {
            HashMap<String, String> param = new HashMap<>();
            param.put("randomNumberStr", encryptionUtil.encrypt(randomNumber));
            param.put("randomNumberStrSign", encryptionUtil.sign(randomNumber));
            // 构建你的请求头
            HttpHeaders headers = new HttpHeaders();
            // 组合请求头与请求体参数
            HttpEntity<String> requestEntity = new HttpEntity(param, headers);
            //获取IMEI对应的ChipId
            String result = restTemplate.postForObject(mesServerSource.getGetStorageKeyUrl(), requestEntity, String.class);
            CommResponse commResponse = JSON.parseObject(result, CommResponse.class);
            if (commResponse.getCode() == 200) {
                ServerStorageKey data = JSON.parseObject(commResponse.getData().toString(), ServerStorageKey.class);
                String sign = data.getSign();
                String key = encryptionUtil.decode(data.getEncryptStr());
                Boolean verify = encryptionUtil.verify(key, sign);
                if (verify) {
                    return key;
                } else {
                    throw new CustomException(NetCodeEnum.STORAGE_KEY_ERROR);
                }
            } else {
                throw new CustomException(commResponse.getMessage());
            }
        } catch (Exception e) {
            log.info("MES服务异常：{}", e.getMessage());
            throw new CustomException(NetCodeEnum.MES_ERROR);
        }
    }
}
