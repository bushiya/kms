package com.transsion.authenticationfactory.module.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.transsion.authenticationfactory.infrastructure.advice.CommResponse;
import com.transsion.authenticationfactory.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationfactory.infrastructure.exception.CustomException;
import com.transsion.authenticationfactory.infrastructure.utils.EncryptionUtil;
import com.transsion.authenticationfactory.infrastructure.utils.SHAUtils;
import com.transsion.authenticationfactory.module.device.repository.dao.ServerCertRecordDao;
import com.transsion.authenticationfactory.module.device.repository.entity.ServerCertRecordEntity;
import com.transsion.authenticationfactory.module.device.repository.mapper.ServerCertRecordMapper;
import com.transsion.authenticationfactory.module.device.repository.resource.MesServerSource;
import com.transsion.authenticationfactory.module.device.service.MesAuthService;
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

    @Override
    public String getFactoryCert(String csr) {
        try {
            HashMap<String, String> param = new HashMap<>();
            String factoryCsr = EncryptionUtil.encrypt(csr);
            param.put("factoryCsr", factoryCsr);
            param.put("factoryCsrSign", EncryptionUtil.sign(csr));
            // 构建你的请求头
            HttpHeaders headers = new HttpHeaders();
            // 组合请求头与请求体参数
            HttpEntity<String> requestEntity = new HttpEntity(param, headers);
            //获取IMEI对应的ChipId
            String result = restTemplate.postForObject(mesServerSource.getGetCertUrl(), requestEntity, String.class);
            CommResponse commResponse = JSON.parseObject(result, CommResponse.class);
            if (commResponse.getCode() == 200) {
                ServerCertRecordEntity data = JSON.parseObject(commResponse.getData().toString(), ServerCertRecordEntity.class);
                data.setServerTag(0);
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
}
