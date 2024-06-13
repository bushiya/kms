package com.transsion.authentication.model.bean.resp;

import lombok.Data;

import java.util.Date;

/**
 * 响应：设备应用查询实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class DeviceAppResp {

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
