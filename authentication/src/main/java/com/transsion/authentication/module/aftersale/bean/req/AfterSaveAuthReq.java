package com.transsion.authentication.module.aftersale.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 售后认证请求
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Data
public class AfterSaveAuthReq {
    @NotNull(message = "OA账号token为空")
    private String token;

    @NotNull(message = "售后特征码为空")
    private String featureCode;

    @NotNull(message = "设备证书请求文件为空")
    private String deviceCsr;

    @NotNull(message = "设备证书请求文件签名为空")
    private String deviceCsrSign;

    @NotNull(message = "随机流水号为空")
    private String random;

    @NotNull(message = "随机流水号签名为空")
    private String randomSign;
}
