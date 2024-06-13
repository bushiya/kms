package com.transsion.authentication.module.auth.bean.resp;

import lombok.Data;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/28
 */
@Data
public class SignResp {
    private String scene;
    private String algorithmTag;
    private String sign;
}
