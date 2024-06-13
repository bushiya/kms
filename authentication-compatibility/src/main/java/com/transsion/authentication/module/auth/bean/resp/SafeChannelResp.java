package com.transsion.authentication.module.auth.bean.resp;

import lombok.Data;

/**
 * @Description: 建立可信通道 返回体
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Data
public class SafeChannelResp {
    /**
     * 认证服务公钥证书
     */
    private String authCert;
    /**
     * 设备公钥加密的 16位随机数
     */
    private String authRandomNumber;
    /**
     * 签名
     */
    private String sign;
}
