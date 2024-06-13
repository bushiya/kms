package com.transsion.authentication.module.da.controller.req;

import lombok.Data;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/5/29
 */
@Data
public class DaReq {
    private Integer scene;
    private String verifyData;
    private String encodeRandomNumber;
    private String sign;
}
