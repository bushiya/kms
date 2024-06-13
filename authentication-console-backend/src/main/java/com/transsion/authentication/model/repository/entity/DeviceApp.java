package com.transsion.authentication.model.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 设备应用信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceApp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * app 公钥
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
