package com.transsion.authentication.module.auth.repository.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.transsion.authentication.infrastructure.annotation.IsEncode;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 设备衍生密钥 非对称密钥
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Data
@TableName("tb_device_asymmetry_key")
public class DeviceAsymmetrySecretKeyEntity {
    @TableId
    private Long id;
    private String sessionId;
    private String scene;
    /**
     * 手机公钥
     */
    @IsEncode
    private String phonePublicKey;
    /**
     * 服务公钥
     */
    @IsEncode
    private String serverPublicKey;
    /**
     * 服务私钥
     */
    @IsEncode
    private String serverPrivateKey;
    private Date createTime;
    private Date updateTime;

}
