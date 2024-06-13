package com.transsion.authentication.model.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.model.bean.req.DeviceAppQueryReq;
import com.transsion.authentication.model.bean.req.DeviceAppSaveReq;
import com.transsion.authentication.model.bean.req.DeviceAppStatusReq;
import com.transsion.authentication.model.bean.resp.DeviceAppResp;
import com.transsion.authentication.model.service.DeviceAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 设备应用控制器
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@RestController
@RequestMapping("/legal-app")
public class DeviceAppController {

    @Autowired
    private DeviceAppService deviceAppService;

    /**
     * 新增应用
     *
     * @param saveReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public CommResponse addApp(@RequestBody @Valid DeviceAppSaveReq saveReq) {
        return CommResponse.success(deviceAppService.addApp(saveReq));
    }

    /**
     * 变更状态
     *
     * @param statusReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public CommResponse changeStatus(@RequestBody @Valid DeviceAppStatusReq statusReq) {
        deviceAppService.changeStatus(statusReq);
        return CommResponse.success();
    }

    /**
     * 查询应用接入设置
     *
     * @param queryReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommResponse<Page<DeviceAppResp>> search(@RequestBody DeviceAppQueryReq queryReq) {
        return CommResponse.success(deviceAppService.search(queryReq));
    }

}

