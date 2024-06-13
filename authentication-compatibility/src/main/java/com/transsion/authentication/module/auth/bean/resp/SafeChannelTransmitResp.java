package com.transsion.authentication.module.auth.bean.resp;

import lombok.Data;

/**
 * @Description: 建立可信通道2 返回体
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Data
public class SafeChannelTransmitResp {
    /**
     * 设备公钥证书加密后的 通信对称密钥
     */
    private String appSocketKey;
    /**
     * 通信对称密钥的校验值（加密第一次会话的随机数，密文取前八位）
     */
    private String appSocketKeySign;
    /**
     * 设备公钥证书加密后的 通信签名对称密钥
     */
    private String appMacKey;
    /**
     * 通信签名对称密钥的校验值 （加密第一次会话的随机数，密文取前八位）
     */
    private String appMacKeySign;
}
