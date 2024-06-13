package com.transsion.authenticationsdk.module.safety.body;

/**
 * @Description: 验签请求体
 * @Author jiakang.chen
 * @Date 2023/7/15
 */
public class VerifyReq extends BaseReq {
    private String metaMessage;
    private String messageSign;

    public String getMetaMessage() {
        return metaMessage;
    }

    public void setMetaMessage(String metaMessage) {
        this.metaMessage = metaMessage;
    }

    public String getMessageSign() {
        return messageSign;
    }

    public void setMessageSign(String messageSign) {
        this.messageSign = messageSign;
    }
}
