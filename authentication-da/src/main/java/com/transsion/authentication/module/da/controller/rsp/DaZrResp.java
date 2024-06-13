package com.transsion.authentication.module.da.controller.rsp;

import lombok.Data;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/5/24
 */
@Data
public class DaZrResp {
    private String pcSign;
    private String phoneSign;
    private String randomNumber;
}
