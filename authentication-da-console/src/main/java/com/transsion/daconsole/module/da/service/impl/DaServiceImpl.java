package com.transsion.daconsole.module.da.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.transsion.daconsole.module.da.bean.req.DeviceListReq;
import com.transsion.daconsole.module.da.repository.entity.DaEntity;
import com.transsion.daconsole.module.da.repository.mapper.DaMapper;
import com.transsion.daconsole.module.da.service.DaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/10/19
 */
@Service
public class DaServiceImpl extends ServiceImpl<DaMapper, DaEntity> implements DaService {
    @Override
    public PageInfo<DaEntity> deviceList(DeviceListReq req) {
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<DaEntity> daList = this.list(
                new QueryWrapper<DaEntity>()
                        .eq(StringUtils.isNotBlank(req.getChipId()), "chip_id", req.getChipId())
                        .eq(StringUtils.isNotBlank(req.getFeatureCode()), "feature_code", req.getFeatureCode())
                        .eq(StringUtils.isNotBlank(req.getScene()), "scene", req.getScene())
                        .eq(StringUtils.isNotBlank(req.getChipOriginalId()), "chip_original_id", req.getChipOriginalId())
                        .eq(StringUtils.isNotBlank(req.getDeviceBrand()), "device_brand", req.getDeviceBrand())
                        .eq(StringUtils.isNotBlank(req.getDeviceModel()), "device_model", req.getDeviceModel())
                        .eq(StringUtils.isNotBlank(req.getToolVersion()), "tool_version", req.getScene())
                        .eq(StringUtils.isNotBlank(req.getWorkOrderID()), "work_order_id", req.getScene())
                        .eq(StringUtils.isNotBlank(req.getSoftwareVersion()), "software_version", req.getScene())
        );
        return PageInfo.of(daList);
    }
}
