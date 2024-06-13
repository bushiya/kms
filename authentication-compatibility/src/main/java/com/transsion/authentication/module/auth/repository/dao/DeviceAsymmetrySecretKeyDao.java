package com.transsion.authentication.module.auth.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.DeviceAsymmetrySecretKeyEntity;
import com.transsion.authentication.module.auth.repository.mapper.DeviceAsymmetrySecretKeyMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Component
public class DeviceAsymmetrySecretKeyDao {
    @Autowired
    DeviceAsymmetrySecretKeyMapper mapper;

    public DeviceAsymmetrySecretKeyEntity selectBySessionIdAndScene(String sessionId, String scene) {
        DeviceAsymmetrySecretKeyEntity deviceAsymmetrySecretKeyEntity = mapper.selectOne(new QueryWrapper<DeviceAsymmetrySecretKeyEntity>().eq("session_id", sessionId).eq("scene", scene));
        if (ObjectUtils.isEmpty(deviceAsymmetrySecretKeyEntity)) {
            throw new CustomException(NetCodeEnum.STORAGE_KEY_NOT_ERROR);
        }
        return deviceAsymmetrySecretKeyEntity;
    }
}
