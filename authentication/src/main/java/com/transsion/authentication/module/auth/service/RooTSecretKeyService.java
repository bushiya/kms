package com.transsion.authentication.module.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.transsion.authentication.module.auth.repository.entity.DeviceRootSecretKeyEntity;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/20
 */
public interface RooTSecretKeyService extends IService<DeviceRootSecretKeyEntity> {
    void saveUpdate(DeviceRootSecretKeyEntity rootKey);
}
