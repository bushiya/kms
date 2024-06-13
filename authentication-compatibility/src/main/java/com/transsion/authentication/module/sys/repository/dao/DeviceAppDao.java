package com.transsion.authentication.module.sys.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.sys.repository.entity.DeviceAppEntity;
import com.transsion.authentication.module.sys.repository.mapper.DeviceAppMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 设备应用 Mapper 接口
 *
 * @author jiakang.chen
 * @since 2023-05-12
 */
@Component
public class DeviceAppDao {
    @Autowired
    DeviceAppMapper mapper;

    public DeviceAppEntity selectByAppId(String appId) {
        DeviceAppEntity deviceAppEntity = mapper.selectOne(new QueryWrapper<DeviceAppEntity>().eq("app_id", appId).eq("status", "2"));
        if (ObjectUtils.isEmpty(deviceAppEntity)) {
            throw new CustomException(NetCodeEnum.CUT_IN_ERROR);
        }
        return deviceAppEntity;
    }
}
