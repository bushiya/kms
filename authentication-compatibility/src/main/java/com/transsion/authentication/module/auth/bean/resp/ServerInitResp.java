package com.transsion.authentication.module.auth.bean.resp;

import lombok.Data;

/**
 * @Description: 业务方服务器初始化 返回体
 * @Author jiakang.chen
 * @Date 2023/7/11
 */
@Data
public class ServerInitResp {
    private String secretKey;
    private String sign;
}
