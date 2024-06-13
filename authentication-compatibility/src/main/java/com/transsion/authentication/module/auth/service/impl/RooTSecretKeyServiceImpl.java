package com.transsion.authentication.module.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.DeviceRootSecretKeyEntity;
import com.transsion.authentication.module.auth.repository.mapper.DeviceRootSecretKeyMapper;
import com.transsion.authentication.module.auth.service.RooTSecretKeyService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/20
 */
@Service
public class RooTSecretKeyServiceImpl extends ServiceImpl<DeviceRootSecretKeyMapper, DeviceRootSecretKeyEntity> implements RooTSecretKeyService {
    @Autowired
    DeviceRootSecretKeyMapper mapper;

    @Override
    public void saveUpdate(DeviceRootSecretKeyEntity rootKey) {
        String sessionId = rootKey.getSessionId();
        DeviceRootSecretKeyEntity entity = mapper.selectOne(new QueryWrapper<DeviceRootSecretKeyEntity>().eq("session_id", sessionId));
        int i = 0;
        if (ObjectUtils.isEmpty(entity)) {
            i = mapper.insert(rootKey);
        } else {
            rootKey.setId(entity.getId());
            i = mapper.updateById(rootKey);
        }
        if (i != 1) {
            throw new CustomException(NetCodeEnum.GENERATE_SECRET_KEY_ERROR);
        }
    }
}
