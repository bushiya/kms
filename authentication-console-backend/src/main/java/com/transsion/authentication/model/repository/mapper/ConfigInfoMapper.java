package com.transsion.authentication.model.repository.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.dto.ConfigInfoQueryDTO;
import com.transsion.authentication.model.bean.req.ConfigInfoQueryReq;
import com.transsion.authentication.model.bean.resp.ConfigInfoQueryResp;
import com.transsion.authentication.model.repository.entity.ConfigInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 配置信息 Mapper 接口
 *
 * @author donghai.yuan
 * @since 2023-05-22
 */
@Repository
public interface ConfigInfoMapper extends BaseMapper<ConfigInfo> {

    IPage<ConfigInfoQueryDTO> queryModel(@Param("pages") Page<ConfigInfoQueryResp> pages, @Param("queryReq") ConfigInfoQueryReq queryReq);

    ConfigInfoQueryDTO queryConfigInfoByNameAndValue(@Param("name") String name, @Param("value") String value);

    ConfigInfoQueryDTO queryConfigInByValue(@Param("value") String value);
}
