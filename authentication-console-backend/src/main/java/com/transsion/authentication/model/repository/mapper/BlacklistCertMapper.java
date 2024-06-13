package com.transsion.authentication.model.repository.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.dto.BlacklistCertQueryDTO;
import com.transsion.authentication.model.bean.req.BlacklistCertQueryReq;
import com.transsion.authentication.model.bean.resp.BlacklistCertQueryResp;
import com.transsion.authentication.model.repository.entity.BlacklistCert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 黑名单证书 Mapper 接口
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Repository
public interface BlacklistCertMapper extends BaseMapper<BlacklistCert> {
    IPage<BlacklistCertQueryDTO> queryCert(@Param("pages") Page<BlacklistCertQueryResp> pages, @Param("queryReq") BlacklistCertQueryReq queryReq);

    BlacklistCertQueryDTO queryBlacklistByName(@Param("name") String name);

    BlacklistCertQueryDTO queryBlacklistByCertValue(@Param("certValue") String certValue);
}
