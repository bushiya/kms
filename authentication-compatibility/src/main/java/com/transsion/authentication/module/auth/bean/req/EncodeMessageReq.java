package com.transsion.authentication.module.auth.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 加密消息 请求体
 * @Author jiakang.chen
 * @Date 2023/6/25
 */
@Data
public class EncodeMessageReq {

    @NotNull(message = "消息为空")
    private String metaMessage;

    @NotNull(message = "随机数为空")
    private String randomNumber;

    @NotNull(message = "密钥场景为空")
    private String scene;

    @NotNull(message = "sessionId为空")
    private String sessionId;

}
