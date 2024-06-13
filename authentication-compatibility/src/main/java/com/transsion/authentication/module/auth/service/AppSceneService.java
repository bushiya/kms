package com.transsion.authentication.module.auth.service;

import com.transsion.authentication.module.auth.repository.entity.AppSceneEntity;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/15
 */
public interface AppSceneService {
    /**
     * 判断业务方是否衍生过场景的密钥，如果不存在则初始化该场景
     *
     * @param appSceneEntity
     */
    void isHasAndSave(AppSceneEntity appSceneEntity);
}
