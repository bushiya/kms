package com.transsion.authentication.module.aftersale.service;

import com.transsion.authentication.module.aftersale.bean.req.AfterSaveAuthReq;
import com.transsion.authentication.module.aftersale.bean.resp.AfterSaveAuthResp;
import com.transsion.authentication.module.aftersale.repository.entity.AfterHeadEntity;

/**
 * @Description: 售后认证服务
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
public interface AfterSaleService {
    /**
     * 设备售后鉴权
     *
     * @param heads 请求头
     * @param req   请求体
     * @return
     */
    AfterSaveAuthResp deviceAuth(AfterHeadEntity heads, AfterSaveAuthReq req) throws Exception;
}
