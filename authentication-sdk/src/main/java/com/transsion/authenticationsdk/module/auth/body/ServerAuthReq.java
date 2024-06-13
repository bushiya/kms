package com.transsion.authenticationsdk.module.auth.body;

/**
 * @Description: 与 KMS 服务器建立安全通道 请求体
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class ServerAuthReq {
    /**
     * 验证信息
     */
    private String verifyMessage;
    /**
     * 随机数（服务集群标识）
     */
    private String randomNumber;

    public String getVerifyMessage() {
        return verifyMessage;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }
}
