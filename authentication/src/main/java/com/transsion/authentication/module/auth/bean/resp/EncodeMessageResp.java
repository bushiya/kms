package com.transsion.authentication.module.auth.bean.resp;

import lombok.Data;

/**
 * @Description: 加密 返回体
 * @Author jiakang.chen
 * @Date 2023/6/29
 */
@Data
public class EncodeMessageResp {
    private String enMessage;
    private String scene;
    private String algorithmTag;
    private String randomNumber;
    private String macMessage;
}
