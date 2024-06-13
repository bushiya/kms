package com.transsion.authenticationfactory.module.device.repository.dao;

import com.transsion.authenticationfactory.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationfactory.infrastructure.exception.CustomException;
import com.transsion.authenticationfactory.module.device.repository.entity.ServerCertRecordEntity;
import com.transsion.authenticationfactory.module.device.repository.mapper.ServerCertRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Component
public class ServerCertRecordDao {
    @Autowired
    ServerCertRecordMapper mapper;

    public void insert(ServerCertRecordEntity data) {
        int insert = mapper.insert(data);
        if (insert != 1) {
            throw new CustomException(NetCodeEnum.CERT_RECORD_ERROR);
        }
    }
}
