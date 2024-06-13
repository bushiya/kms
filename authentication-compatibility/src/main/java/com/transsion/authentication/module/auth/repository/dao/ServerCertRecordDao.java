package com.transsion.authentication.module.auth.repository.dao;

import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.ServerCertRecordEntity;
import com.transsion.authentication.module.auth.repository.mapper.ServerCertRecordMapper;
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
