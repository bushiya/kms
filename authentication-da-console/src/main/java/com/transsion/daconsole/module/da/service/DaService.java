package com.transsion.daconsole.module.da.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.transsion.daconsole.module.da.bean.req.DeviceListReq;
import com.transsion.daconsole.module.da.repository.entity.DaEntity;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/10/19
 */
public interface DaService extends IService<DaEntity> {
    PageInfo<DaEntity> deviceList(DeviceListReq req);
}
