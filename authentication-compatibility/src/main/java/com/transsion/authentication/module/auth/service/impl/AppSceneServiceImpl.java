package com.transsion.authentication.module.auth.service.impl;

import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.dao.AppSceneDao;
import com.transsion.authentication.module.auth.repository.entity.AppSceneEntity;
import com.transsion.authentication.module.auth.service.AppSceneService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/15
 */
@Service
public class AppSceneServiceImpl implements AppSceneService {
    @Autowired
    AppSceneDao appSceneDao;

    @Override
    public void isHasAndSave(AppSceneEntity appSceneEntity) {
        AppSceneEntity appSceneEntity1 = appSceneDao.selectByAppIdAndSceneNoException(appSceneEntity.getAppId(), appSceneEntity.getScene());
        if (ObjectUtils.isEmpty(appSceneEntity1)) {
            appSceneEntity.setCreateTime(new Date());
            appSceneEntity.setUpdateTime(new Date());
            int save = appSceneDao.save(appSceneEntity);
            if (save != 1) {
                throw new CustomException(NetCodeEnum.SECRET_KEY_SCENE_SAVE_ERROR);
            }
        }
    }
}
