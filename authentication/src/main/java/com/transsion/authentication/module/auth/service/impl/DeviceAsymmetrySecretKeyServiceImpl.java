package com.transsion.authentication.module.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.DeviceAsymmetrySecretKeyEntity;
import com.transsion.authentication.module.auth.repository.mapper.DeviceAsymmetrySecretKeyMapper;
import com.transsion.authentication.module.auth.service.DeviceAsymmetrySecretKeyService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Service
public class DeviceAsymmetrySecretKeyServiceImpl extends ServiceImpl<DeviceAsymmetrySecretKeyMapper, DeviceAsymmetrySecretKeyEntity> implements DeviceAsymmetrySecretKeyService {
    @Autowired
    DeviceAsymmetrySecretKeyMapper mapper;

    @Override
    public void saveOrUpdateSecretKey(DeviceAsymmetrySecretKeyEntity data) {
        DeviceAsymmetrySecretKeyEntity deviceAsymmetrySecretKeyEntity = mapper.selectOne(new QueryWrapper<DeviceAsymmetrySecretKeyEntity>().eq("session_id", data.getSessionId()).eq("scene", data.getScene()));
        if (ObjectUtils.isEmpty(deviceAsymmetrySecretKeyEntity)) {
            int insert = mapper.insert(data);
            if (insert != 1) {
                throw new CustomException(NetCodeEnum.SECRET_KEY_SAVE_ERROR);
            }
        } else {
            data.setId(deviceAsymmetrySecretKeyEntity.getId());
            int i = mapper.updateById(data);
            if (i != 1) {
                throw new CustomException(NetCodeEnum.SECRET_KEY_SAVE_ERROR);
            }
        }
    }
}
