package com.transsion.authentication.module.da.controller.rsp;

import lombok.Data;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/5/24
 */
@Data
public class DaResp {
    private String encodePc;
    private String sign;
    private String randomNumber;
}
