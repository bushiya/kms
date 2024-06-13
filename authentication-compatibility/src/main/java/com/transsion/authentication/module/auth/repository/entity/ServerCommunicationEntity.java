package com.transsion.authentication.module.auth.repository.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.transsion.authentication.infrastructure.annotation.IsEncode;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 业务方服务器 安全通道 密钥
 * @Author jiakang.chen
 * @Date 2023/7/11
 */
@Data
@TableName("tb_server_communication")
public class ServerCommunicationEntity {
    @TableId
    private Long id;
    private String appId;
    /**
     * 标识 业务方的集群
     */
    private String serverTag;
    @IsEncode
    private String secretKey;
    private Date createTime;
    private Date updateTime;
}
