package com.transsion.authentication.module.sys.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.transsion.authentication.module.sys.repository.entity.CertBlacklistEntity;
import com.transsion.authentication.module.sys.repository.mapper.CertBlacklistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 黑名单证书 Mapper 接口
 *
 * @author jiakang.chen
 * @since 2023-05-12
 */
@Component
public class CertBlacklistDao {
    @Autowired
    CertBlacklistMapper mapper;

    public CertBlacklistEntity selectByCert(String certSha256) {
        CertBlacklistEntity certBlacklistEntity = mapper.selectOne(new QueryWrapper<CertBlacklistEntity>().eq("cert_value", certSha256).eq("status", "2"));
        return certBlacklistEntity;
    }
}
