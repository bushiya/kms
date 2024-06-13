package com.transsion.authentication.module.auth.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.DeviceSymmetrySecretKeyEntity;
import com.transsion.authentication.module.auth.repository.mapper.DeviceSymmetrySecretKeyMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Component
public class DeviceSymmetrySecretKeyDao {
    @Autowired
    DeviceSymmetrySecretKeyMapper mapper;

    public DeviceSymmetrySecretKeyEntity selectBySessionIdAndScene(String sessionId, String scene) {
        DeviceSymmetrySecretKeyEntity deviceSymmetrySecretKeyEntity = mapper.selectOne(new QueryWrapper<DeviceSymmetrySecretKeyEntity>().eq("session_id", sessionId).eq("scene", scene));
        if (ObjectUtils.isEmpty(deviceSymmetrySecretKeyEntity)) {
            throw new CustomException(NetCodeEnum.STORAGE_KEY_NOT_ERROR);
        }
        return deviceSymmetrySecretKeyEntity;
    }

}
