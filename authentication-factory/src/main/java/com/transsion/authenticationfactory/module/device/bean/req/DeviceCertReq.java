package com.transsion.authenticationfactory.module.device.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@Data
public class DeviceCertReq {
    @NotNull(message = "设备证书请求文件为空")
    private String deviceCsr;
}
