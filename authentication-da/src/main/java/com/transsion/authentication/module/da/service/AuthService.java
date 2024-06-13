package com.transsion.authentication.module.da.service;

import com.transsion.authentication.module.da.repository.entity.AfterSaveEntity;
import com.transsion.authentication.module.da.repository.entity.FansEntity;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/10
 */
public interface AuthService {
    AfterSaveEntity featureCodeAuth(String featureCode);

    Boolean oaTokenAuth(String rToken, String uToken);

    FansEntity palmIdAuth(String palmId);

}
