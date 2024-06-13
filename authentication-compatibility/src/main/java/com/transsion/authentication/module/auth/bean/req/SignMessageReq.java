package com.transsion.authentication.module.auth.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 签名 请求体
 * @Author jiakang.chen
 * @Date 2023/6/25
 */
@Data
public class SignMessageReq {
    @NotNull(message = "消息体为空")
    private String metaMessage;

    @NotNull(message = "密钥场景为空")
    private String scene;

    @NotNull(message = "sessionId为空")
    private String sessionId;

}
