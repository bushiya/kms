package com.transsion.authentication.module.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.transsion.authentication.module.auth.repository.entity.DeviceAsymmetrySecretKeyEntity;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
public interface DeviceAsymmetrySecretKeyService extends IService<DeviceAsymmetrySecretKeyEntity> {
    void saveOrUpdateSecretKey(DeviceAsymmetrySecretKeyEntity data);
}
