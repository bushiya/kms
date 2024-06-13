package com.transsion.authentication.module.aftersale.bean.resp;

import lombok.Data;

/**
 * @Description: 售后认证响应
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Data
public class AfterSaveAuthResp {
    /**
     * 公网证书
     */
    private String factoryCert;
    /**
     * 设备证书
     */
    private String deviceCert;
    /**
     * 流水号签名
     */
    private String randomSign;
}
