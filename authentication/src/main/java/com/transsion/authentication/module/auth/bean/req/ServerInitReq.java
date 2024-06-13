package com.transsion.authentication.module.auth.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 业务方服务器初始化 请求体
 * @Author jiakang.chen
 * @Date 2023/7/11
 */
@Data
public class ServerInitReq {
    @NotNull(message = "验证信息为空")
    private String verifyMessage;

    @NotNull(message = "随机数为空")
    private String randomNumber;
}
