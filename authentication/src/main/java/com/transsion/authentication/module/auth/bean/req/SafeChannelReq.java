package com.transsion.authentication.module.auth.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 建立可信通道请求体
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Data
public class SafeChannelReq {
    @NotNull(message = "工厂证书为空")
    private String factoryCert;

    @NotNull(message = "设备证书为空")
    private String deviceCert;

    @NotNull(message = "通信标识为空")
    private String communicationTag;

    @NotNull(message = "签名为空")
    private String sign;
}
