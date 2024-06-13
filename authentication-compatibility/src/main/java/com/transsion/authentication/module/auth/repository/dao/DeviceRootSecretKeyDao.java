package com.transsion.authentication.module.auth.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.DeviceRootSecretKeyEntity;
import com.transsion.authentication.module.auth.repository.mapper.DeviceRootSecretKeyMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/29
 */
@Component
public class DeviceRootSecretKeyDao {
    @Autowired
    DeviceRootSecretKeyMapper mapper;



    /**
     * 存储设备通信密钥
     *
     * @param rootKey
     */
    public void save(DeviceRootSecretKeyEntity rootKey) throws Exception {
        int insert = mapper.insert(rootKey);
        if (insert == 0) {
            throw new CustomException(NetCodeEnum.GENERATE_STORAGE_KEY_ERROR);
        }
    }

    /**
     * 删除过期 设备通信密钥
     *
     * @param PastDueDate 过期时间
     */
    public void deletePastDueKey(Date PastDueDate) {

    }

    /**
     * 根据 sessionId 查询设备对应的 通信密钥
     *
     * @param sessionId
     * @return
     */
    public DeviceRootSecretKeyEntity getRootKey(String sessionId) {
        DeviceRootSecretKeyEntity deviceRootSecretKeyEntity = mapper.selectOne(new QueryWrapper<DeviceRootSecretKeyEntity>().eq("session_id", sessionId));
        if (ObjectUtils.isEmpty(deviceRootSecretKeyEntity)) {
            throw new CustomException(NetCodeEnum.STORAGE_KEY_NOT_ERROR);
        }
        return deviceRootSecretKeyEntity;
    }

    /**
     * 根据 ID 删除 通信密钥
     *
     * @param sessionId
     */
    public void deleteById(String sessionId) {
        mapper.delete(new QueryWrapper<DeviceRootSecretKeyEntity>().eq("session_id", sessionId));
    }
}
