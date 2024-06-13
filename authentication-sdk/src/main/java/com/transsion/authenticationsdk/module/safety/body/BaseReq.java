package com.transsion.authenticationsdk.module.safety.body;

/**
 * @Description: 公共请求参数
 * @Author jiakang.chen
 * @Date 2023/7/15
 */
public class BaseReq {
    private String sessionId;
    private String scene;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }
}
