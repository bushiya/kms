package com.transsion.authentication.module.auth.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 衍生密钥 请求体
 * @Author jiakang.chen
 * @Date 2023/6/25
 */
@Data
public class DerivedKeyReq {
    @NotNull(message = "密钥索引为空")
    private String encodeKeyIndex;

    @NotNull(message = "随机数为空")
    private String randomNumber;

    @NotNull(message = "mac密文为空")
    private String mac;

    @NotNull(message = "sessionId为空")
    private String sessionId;
}
