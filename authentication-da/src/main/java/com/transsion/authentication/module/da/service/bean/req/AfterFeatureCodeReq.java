package com.transsion.authentication.module.da.service.bean.req;

import lombok.Data;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/10/17
 */
@Data
public class AfterFeatureCodeReq {
    private String newFeatureCode;
    private String oldFeatureCode = "";
    private String verifyCode;
}
