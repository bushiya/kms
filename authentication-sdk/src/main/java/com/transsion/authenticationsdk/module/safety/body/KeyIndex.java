package com.transsion.authenticationsdk.module.safety.body;

/**
 * @Description: 衍生密钥索引对象
 * @Author jiakang.chen
 * @Date 2023/7/15
 */
public class KeyIndex {
    /**
     * 衍生密钥对应的场景
     */
    private String sc;
    /**
     * 衍生密钥的算法
     */
    private Integer algo;
    /**
     * 手机公钥
     */
    private String dpub;
    /**
     * 托管方
     */
    private Integer kLoc;
    /**
     * 业务方 appId
     */
    private String appId;

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public Integer getAlgo() {
        return algo;
    }

    public void setAlgo(Integer algo) {
        this.algo = algo;
    }

    public String getDpub() {
        return dpub;
    }

    public void setDpub(String dpub) {
        this.dpub = dpub;
    }

    public Integer getkLoc() {
        return kLoc;
    }

    public void setkLoc(Integer kLoc) {
        this.kLoc = kLoc;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
