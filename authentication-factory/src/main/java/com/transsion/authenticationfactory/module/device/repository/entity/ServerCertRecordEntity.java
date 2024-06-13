package com.transsion.authenticationfactory.module.device.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@Data
@TableName("tb_server_cert_record")
public class ServerCertRecordEntity {
    @TableId
    private Integer id;
    private String cert;
    private Integer serverTag;
    private String serverMac;
    @TableField("server_ip")
    private String serverIP;
    private Date createTime;
    private Date updateTime;
}
