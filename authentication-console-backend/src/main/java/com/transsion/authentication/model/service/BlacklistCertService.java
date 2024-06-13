package com.transsion.authentication.model.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.req.BlacklistCertQueryReq;
import com.transsion.authentication.model.bean.req.BlacklistCertSaveReq;
import com.transsion.authentication.model.bean.req.BlacklistCertStatusReq;
import com.transsion.authentication.model.bean.resp.BlacklistCertQueryResp;
import com.transsion.authentication.model.repository.entity.BlacklistCert;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 黑名单证书业务类
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
public interface BlacklistCertService extends IService<BlacklistCert> {

    void insertAbnormal(BlacklistCertSaveReq saveReq);

    void changeStatus(BlacklistCertStatusReq statusReq);

    Page<BlacklistCertQueryResp> queryCert(BlacklistCertQueryReq queryReq);
}
