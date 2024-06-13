package com.transsion.authentication.module.auth.repository.entity;

import lombok.Data;

/**
 * @Description: 衍生密钥 密钥索引
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Data
public class KeyIndex {
//    private String scene;
//    private Integer algorithmCode;
//    private String publicKey;
//    private Integer trusteeshipCode;
//    private String appId;

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
    private String dPub;
    /**
     * 托管方
     */
    private Integer kLoc;
    /**
     * 业务方 appId
     */
    private String appId;
}
