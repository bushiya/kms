package com.transsion.authentication.model.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.dto.BlacklistCertQueryDTO;
import com.transsion.authentication.model.bean.req.BlacklistCertQueryReq;
import com.transsion.authentication.model.bean.resp.BlacklistCertQueryResp;
import com.transsion.authentication.model.repository.entity.BlacklistCert;
import com.transsion.authentication.model.repository.mapper.BlacklistCertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 黑名单数据层
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Component
public class BlacklistCertDao {

    @Autowired
    private BlacklistCertMapper blacklistCertMapper;

    /**
     * 新增证书黑名单
     *
     * @param blacklistCert 请求参数
     */
    public void insertAbnormal(BlacklistCert blacklistCert) {
        blacklistCert.setCreateTime(new Date());
        blacklistCert.setUpdateTime(new Date());
        blacklistCertMapper.insert(blacklistCert);
    }

    /**
     * 根据黑名单证书ID查询证书信息
     *
     * @param id 请求参数
     * @return 返回结果
     */
    public BlacklistCert queryById(Long id) {
        return blacklistCertMapper.selectById(id);
    }

    /**
     * 更新黑名单证书
     *
     * @param blacklistCert 请求参数
     */
    public void updateBlacklistCert(BlacklistCert blacklistCert) {
        blacklistCertMapper.updateById(blacklistCert);
    }


    /**
     * 查询黑名单证书信息
     *
     * @param pages    请求参数
     * @param queryReq 请求参数
     * @return 返回结果
     */
    public IPage<BlacklistCertQueryDTO> queryCert(Page<BlacklistCertQueryResp> pages, BlacklistCertQueryReq queryReq) {
        return blacklistCertMapper.queryCert(pages, queryReq);
    }

    /**
     * 按黑名单名称查询信息
     *
     * @param name 请求参数
     * @return 返回结果
     */
    public BlacklistCertQueryDTO queryBlacklistByName(String name) {
        return blacklistCertMapper.queryBlacklistByName(name);
    }

    /**
     * 按黑名单证书值查询信息
     *
     * @param certValue 请求参数
     * @return 返回结果
     */
    public BlacklistCertQueryDTO queryBlacklistByCertValue(String certValue) {
        return blacklistCertMapper.queryBlacklistByCertValue(certValue);
    }
}
