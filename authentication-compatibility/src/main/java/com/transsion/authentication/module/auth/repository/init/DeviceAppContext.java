package com.transsion.authentication.module.auth.repository.init;

import com.alibaba.fastjson2.JSON;
import com.transsion.authentication.infrastructure.utils.RedisUtil;
import com.transsion.authentication.module.sys.repository.dao.DeviceAppDao;
import com.transsion.authentication.module.sys.repository.entity.DeviceAppEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/10
 */
@Slf4j
@Component
public class DeviceAppContext {

    @Autowired
    DeviceAppDao deviceAppDao;

    public DeviceAppEntity getDeviceApp(String appId) {
        String s = RedisUtil.StringOps.get(appId);
        if (StringUtils.isBlank(s)) {
            DeviceAppEntity deviceAppEntities = deviceAppDao.selectByAppId(appId);
            RedisUtil.StringOps.set(appId, JSON.toJSONString(deviceAppEntities));
            return deviceAppEntities;
        }
        DeviceAppEntity deviceAppEntity = JSON.parseObject(s, DeviceAppEntity.class);
        return deviceAppEntity;
    }


}
