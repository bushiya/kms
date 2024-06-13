package com.transsion.authentication.model.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.req.ConfigInfoQueryReq;
import com.transsion.authentication.model.bean.req.ConfigInfoSaveReq;
import com.transsion.authentication.model.bean.req.ConfigInfoStatusReq;
import com.transsion.authentication.model.bean.resp.ConfigInfoQueryResp;
import com.transsion.authentication.model.repository.entity.ConfigInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 黑名单机型配置信息业务类
 *
 * @author donghai.yuan
 * @since 2023-05-22
 */
public interface ConfigInfoService extends IService<ConfigInfo> {

    void insertAbnormal(ConfigInfoSaveReq saveReq);

    void changeStatus(ConfigInfoStatusReq statusReq);

    Page<ConfigInfoQueryResp> queryModel(ConfigInfoQueryReq queryReq);
}
