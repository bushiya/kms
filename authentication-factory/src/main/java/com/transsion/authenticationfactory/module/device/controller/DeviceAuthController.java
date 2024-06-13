package com.transsion.authenticationfactory.module.device.controller;

import com.transsion.authenticationfactory.infrastructure.advice.CommResponse;
import com.transsion.authenticationfactory.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationfactory.infrastructure.exception.CustomException;
import com.transsion.authenticationfactory.infrastructure.utils.EncryptionUtil;
import com.transsion.authenticationfactory.module.device.bean.req.DeviceCertReq;
import com.transsion.authenticationfactory.module.device.bean.resp.DeviceCertResp;
import com.transsion.authenticationfactory.module.device.service.DeviceAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@RestController
@RequestMapping("device")
public class DeviceAuthController {

    @Autowired
    DeviceAuthService deviceAuthService;

    /**
     * 获取设备证书
     *
     * @return
     */
    @PostMapping("get-device-cert")
    public CommResponse<DeviceCertResp> getDeviceCert(@RequestBody @Validated DeviceCertReq req) throws Exception {
        String deviceCsr = req.getDeviceCsr();
        return CommResponse.success(deviceAuthService.getDeviceCert(deviceCsr));
    }
}
