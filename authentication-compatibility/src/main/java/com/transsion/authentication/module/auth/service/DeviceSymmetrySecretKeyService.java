package com.transsion.authentication.module.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.transsion.authentication.module.auth.repository.entity.DeviceSymmetrySecretKeyEntity;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
public interface DeviceSymmetrySecretKeyService extends IService<DeviceSymmetrySecretKeyEntity> {
    void saveOrUpdateSecretKey(DeviceSymmetrySecretKeyEntity data);
}
