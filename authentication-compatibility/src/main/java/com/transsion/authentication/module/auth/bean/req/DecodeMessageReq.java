package com.transsion.authentication.module.auth.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 解密消息 请求体
 * @Author jiakang.chen
 * @Date 2023/6/25
 */
@Data
public class DecodeMessageReq {
    @NotNull(message = "消息密文为空")
    private String enMessage;

//    @NotNull(message = "随机数为空")
    private String randomNumber;

//    @NotNull(message = "mac密文为空")
    private String macMessage;

    @NotNull(message = "场景为空")
    private String scene;

    @NotNull(message = "sessionId为空")
    private String sessionId;
}
