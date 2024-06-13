package com.transsion.authentication.module.auth.repository.entity;

import lombok.Data;

/**
 * @Description: 服务存储加密密钥（MES服务返回值）
 * @Author jiakang.chen
 * @Date 2023/6/25
 */
@Data
public class ServerStorageKey {
    private String encryptStr;
    private String sign;
}
