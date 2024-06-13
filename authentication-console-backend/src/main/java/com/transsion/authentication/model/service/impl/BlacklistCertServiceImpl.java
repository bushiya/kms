package com.transsion.authentication.model.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.infrastructure.enums.StatusEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.enums.NetCodeEnum;
import com.transsion.authentication.infrastructure.utils.EncryptSha256Util;
import com.transsion.authentication.infrastructure.utils.ModelMapperUtils;
import com.transsion.authentication.model.bean.dto.BlacklistCertQueryDTO;
import com.transsion.authentication.model.bean.req.BlacklistCertQueryReq;
import com.transsion.authentication.model.bean.req.BlacklistCertSaveReq;
import com.transsion.authentication.model.bean.req.BlacklistCertStatusReq;
import com.transsion.authentication.model.bean.resp.BlacklistCertQueryResp;
import com.transsion.authentication.model.repository.dao.BlacklistCertDao;
import com.transsion.authentication.model.repository.entity.BlacklistCert;
import com.transsion.authentication.model.repository.mapper.BlacklistCertMapper;
import com.transsion.authentication.model.service.BlacklistCertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * 黑名单证书实现类
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Service
public class BlacklistCertServiceImpl extends ServiceImpl<BlacklistCertMapper, BlacklistCert> implements BlacklistCertService {

    @Autowired
    private BlacklistCertDao blacklistCertDao;

    /**
     * 新增证书黑名单
     *
     * @param saveReq 请求参数
     */
    @Override
    public void insertAbnormal(BlacklistCertSaveReq saveReq) {
        BlacklistCert blacklistCert = new BlacklistCert();
        blacklistCert.setName(saveReq.getName());
        blacklistCert.setCertValue(EncryptSha256Util.getSha256Str(saveReq.getCertValue()));
        blacklistCert.setStatus(StatusEnum.ENABLE.getCode());
        //2.验证数据
        verificationData(blacklistCert);
        blacklistCertDao.insertAbnormal(blacklistCert);
    }

    /**
     * 校验数据
     *
     * @param blacklistCert 请求参数
     */
    private void verificationData(BlacklistCert blacklistCert) {
        BlacklistCertQueryDTO byName = blacklistCertDao.queryBlacklistByName(blacklistCert.getName());
        if (!ObjectUtils.isEmpty(byName)) {
            throw new CustomException(NetCodeEnum.BLACKLIST_NAME_DATA_ALREADY_EXISTS.getCode(), NetCodeEnum.BLACKLIST_NAME_DATA_ALREADY_EXISTS.getMessageKey());
        }
        BlacklistCertQueryDTO certValue = blacklistCertDao.queryBlacklistByCertValue(blacklistCert.getCertValue());
        if (!ObjectUtils.isEmpty(certValue)) {
            throw new CustomException(NetCodeEnum.BLACKLIST_CERT_VALUE_DATA_ALREADY_EXISTS.getCode(), NetCodeEnum.BLACKLIST_CERT_VALUE_DATA_ALREADY_EXISTS.getMessageKey());
        }
    }

    /**
     * 证书变更状态
     *
     * @param statusReq 请求参数
     */
    @Override
    public void changeStatus(BlacklistCertStatusReq statusReq) {
        BlacklistCert blacklistCert = blacklistCertDao.queryById(statusReq.getId());
        if (ObjectUtils.isEmpty(blacklistCert)) {
            throw new CustomException(NetCodeEnum.DATA_ALREADY_NOT_EXISTS.getCode(), NetCodeEnum.DATA_ALREADY_NOT_EXISTS.getMessageKey());
        }
        blacklistCert.setUpdateTime(new Date());
        blacklistCert.setStatus(statusReq.getStatus());
        blacklistCertDao.updateBlacklistCert(blacklistCert);
    }

    /**
     * 查询黑名单证书
     *
     * @param queryReq 请求参数
     * @return 返回结果
     */
    @Override
    public Page<BlacklistCertQueryResp> queryCert(BlacklistCertQueryReq queryReq) {
        Page<BlacklistCertQueryResp> pages = new Page<>(queryReq.getPageNo(), queryReq.getPageSize());
        //2.查询应用配置信息
        IPage<BlacklistCertQueryDTO> iPage = blacklistCertDao.queryCert(pages, queryReq);
        if (ObjectUtils.isEmpty(iPage)) {
            return new Page<>();
        }
        pages.setRecords(ModelMapperUtils.map(iPage.getRecords(), BlacklistCertQueryResp.class));
        pages.setTotal(iPage.getTotal());
        pages.setSize(iPage.getSize());
        return pages;
    }
}
