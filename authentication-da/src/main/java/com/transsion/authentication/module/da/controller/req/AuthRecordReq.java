package com.transsion.authentication.module.da.controller.req;

import lombok.Data;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2024/3/18
 */
@Data
public class AuthRecordReq {
    private Integer scene;
    private String verifyData;
}
