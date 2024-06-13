package com.transsion.authenticationsdk.module.safety.body;

/**
 * @Description: 衍生密钥对象
 * @Author jiakang.chen
 * @Date 2023/7/15
 */
public class DerivedKey {
    private String sessionId;
    private String mac;
    private String randomNumber;
    private String encodeKeyIndex;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getEncodeKeyIndex() {
        return encodeKeyIndex;
    }

    public void setEncodeKeyIndex(String encodeKeyIndex) {
        this.encodeKeyIndex = encodeKeyIndex;
    }
}
