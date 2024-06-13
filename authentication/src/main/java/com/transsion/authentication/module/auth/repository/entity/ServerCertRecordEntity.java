package com.transsion.authentication.module.auth.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 服务初始化证书记录表
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@Data
@TableName("tb_server_cert_record")
public class ServerCertRecordEntity {
    @TableId
    private Integer id;
    private String cert;
    /**
     * 服务标识 0 工厂 1 公网
     */
    private Integer serverTag;
    private String serverMac;
    @TableField("server_ip")
    private String serverIP;
    private Date createTime;
    private Date updateTime;
}
