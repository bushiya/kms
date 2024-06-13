package com.transsion.authenticationsdk.module.safety.body;

/**
 * @Description: 签名请求体
 * @Author jiakang.chen
 * @Date 2023/7/15
 */
public class SignReq extends BaseReq {
    private String metaMessage;

    public String getMetaMessage() {
        return metaMessage;
    }

    public void setMetaMessage(String metaMessage) {
        this.metaMessage = metaMessage;
    }
}
