package com.transsion.authentication.model.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.req.DeviceAppQueryReq;
import com.transsion.authentication.model.bean.req.DeviceAppSaveReq;
import com.transsion.authentication.model.bean.req.DeviceAppStatusReq;
import com.transsion.authentication.model.bean.resp.DeviceAppResp;
import com.transsion.authentication.model.repository.entity.DeviceApp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 设备应用业务类
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
public interface DeviceAppService extends IService<DeviceApp> {

    String addApp(DeviceAppSaveReq saveReq);

    void changeStatus(DeviceAppStatusReq statusReq);

    Page<DeviceAppResp> search(DeviceAppQueryReq queryReq);
}
