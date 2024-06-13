package com.transsion.authentication.module.sys.repository.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/27
 */
@Data
@TableName("device_app")
public class DeviceAppEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 业务方ID
     */
    private String appId;

    /**
     * 业务方名称
     */
    private String appName;

    /**
     * 业务方包名
     */
    private String appPackage;

    /**
     * 业务方签名
     */
    private String appSign;

    /**
     * 业务方描述
     */
    private String appDes;

    /**
     * IP
     */
    private String ip;

    /**
     * app公钥
     */
    private String appPub;
    /**
     * 服务 公钥
     */
    private String serverPublicKey;
    /**
     * 服务 私钥
     */
    private String serverPrivateKey;

    /**
     * 起停用 : 1.禁用  2.启用
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 更新人
     */
    private Long updateUserId;

    /**
     * 更新时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
