package com.transsion.authentication.module.auth.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.AppSceneEntity;
import com.transsion.authentication.module.auth.repository.mapper.AppSceneMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Component
public class AppSceneDao {
    @Autowired
    AppSceneMapper mapper;

    public AppSceneEntity selectByAppIdAndScene(String appId, String scene) {
        AppSceneEntity appSceneEntity = mapper.selectOne(new QueryWrapper<AppSceneEntity>().eq("app_id", appId).eq("scene", scene).eq("state", "0"));
        if (ObjectUtils.isEmpty(appSceneEntity)) {
            throw new CustomException(NetCodeEnum.STORAGE_KEY_NOT_ERROR);
        }
        return appSceneEntity;
    }

    public AppSceneEntity selectByAppIdAndSceneNoException(String appId, String scene) {
        AppSceneEntity appSceneEntity = mapper.selectOne(new QueryWrapper<AppSceneEntity>().eq("app_id", appId).eq("scene", scene).eq("state", "0"));
        return appSceneEntity;
    }

    public int save(AppSceneEntity appSceneEntity) {
        int insert = mapper.insert(appSceneEntity);
        return insert;
    }
}
