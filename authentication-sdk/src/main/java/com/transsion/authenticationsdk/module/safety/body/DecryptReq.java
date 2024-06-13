package com.transsion.authenticationsdk.module.safety.body;

/**
 * @Description: 解密请求体
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class DecryptReq extends BaseReq {
    private String enMessage;
    private String scene;
    private String randomNumber;
    private String macMessage;

    public String getEnMessage() {
        return enMessage;
    }

    public void setEnMessage(String enMessage) {
        this.enMessage = enMessage;
    }

    public String getMacMessage() {
        return macMessage;
    }

    public void setMacMessage(String macMessage) {
        this.macMessage = macMessage;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }
}
