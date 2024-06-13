package com.transsion.authentication.model.repository.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.dto.DeviceAppQueryDTO;
import com.transsion.authentication.model.bean.req.DeviceAppQueryReq;
import com.transsion.authentication.model.bean.resp.DeviceAppResp;
import com.transsion.authentication.model.repository.entity.DeviceApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 设备应用 Mapper 接口
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
public interface DeviceAppMapper extends BaseMapper<DeviceApp> {

    DeviceAppQueryDTO queryByAppId(@Param("deviceApp") DeviceApp deviceApp);

    Integer queryByAppName(@Param("deviceApp") DeviceApp deviceApp);

    Integer queryByAppPackage(@Param("deviceApp") DeviceApp deviceApp);

    IPage<DeviceAppQueryDTO> search(@Param("pages") Page<DeviceAppResp> pages, @Param("deviceApp") DeviceAppQueryReq queryReq);
}
