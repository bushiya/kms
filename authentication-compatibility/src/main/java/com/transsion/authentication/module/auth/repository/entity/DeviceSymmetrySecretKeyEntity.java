package com.transsion.authentication.module.auth.repository.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.transsion.authentication.infrastructure.annotation.IsEncode;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 设备衍生密钥 对称密钥
 * @Author jiakang.chen
 * @Date 2023/6/29
 */
@Data
@TableName("tb_device_symmetry_key")
public class DeviceSymmetrySecretKeyEntity {
    @TableId
    private Long id;
    private String sessionId;
    private String scene;
    @IsEncode
    private String secretKey;
    private Date createTime;
    private Date updateTime;
}
