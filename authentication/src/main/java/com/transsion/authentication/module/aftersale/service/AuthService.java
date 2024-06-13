package com.transsion.authentication.module.aftersale.service;

import com.transsion.authentication.module.aftersale.repository.entity.AfterSaveEntity;

/**
 * @Description: 售后认证服务
 * @Author jiakang.chen
 * @Date 2023/6/10
 */
public interface AuthService {
    /**
     * 售后特征码校验
     * @param featureCode
     * @return
     */
    AfterSaveEntity featureCodeAuth(String featureCode);

    /**
     * OA 账号校验
     * @param rToken
     * @param uToken
     * @return
     */
    Boolean oaTokenAuth(String rToken, String uToken);
}
