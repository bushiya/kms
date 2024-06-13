package com.transsion.authentication.module.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.DeviceSymmetrySecretKeyEntity;
import com.transsion.authentication.module.auth.repository.mapper.DeviceSymmetrySecretKeyMapper;
import com.transsion.authentication.module.auth.service.DeviceSymmetrySecretKeyService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Service
public class DeviceSymmetrySecretKeyServiceImpl extends ServiceImpl<DeviceSymmetrySecretKeyMapper, DeviceSymmetrySecretKeyEntity> implements DeviceSymmetrySecretKeyService {
    @Autowired
    DeviceSymmetrySecretKeyMapper mapper;

    @Override
    public void saveOrUpdateSecretKey(DeviceSymmetrySecretKeyEntity data) {
        DeviceSymmetrySecretKeyEntity deviceSymmetrySecretKeyEntity = mapper.selectOne(new QueryWrapper<DeviceSymmetrySecretKeyEntity>().eq("session_id", data.getSessionId()).eq("scene", data.getScene()));
        if (ObjectUtils.isEmpty(deviceSymmetrySecretKeyEntity)) {
            int insert = mapper.insert(data);
            if (insert != 1) {
                throw new CustomException(NetCodeEnum.SECRET_KEY_SAVE_ERROR);
            }
        } else {
            data.setId(deviceSymmetrySecretKeyEntity.getId());
            int i = mapper.updateById(data);
            if (i != 1) {
                throw new CustomException(NetCodeEnum.SECRET_KEY_SAVE_ERROR);
            }
        }
    }
}
