package com.transsion.authentication.module.auth.repository.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.transsion.authentication.infrastructure.annotation.IsEncode;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 设备通信密钥
 * @Author jiakang.chen
 * @Date 2023/6/29
 */
@Data
@TableName("tb_device_root_secret_key")
public class DeviceRootSecretKeyEntity {
    @TableId
    private Long id;
    private String sessionId;
    @IsEncode
    private String socketKey;
    @IsEncode
    private String macKey;
    private Date createTime;
    private Date updateTime;
}
