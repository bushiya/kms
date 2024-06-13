package com.transsion.authenticationsdk.module.safety.body;

/**
 * @Description: 加密请求体
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class EncryptReq extends BaseReq {
    private String metaMessage;
    private String randomNumber;

    public String getMetaMessage() {
        return metaMessage;
    }

    public void setMetaMessage(String metaMessage) {
        this.metaMessage = metaMessage;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }
}
