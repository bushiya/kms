package com.transsion.authentication.module.da.repository.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_da_data_v2")
public class DaEntity {
    @TableId
    private Integer id;
    /**
     * 工具类型
     */
    private Integer scene;
    /**
     * cpu_id
     */
    private String chipId;
    /**
     * cpu 原始ID
     */
    private String chipOriginalId;
    /**
     * 芯片平台 MTK SPRD
     */
    private String chipPlatform;
    /**
     * 特征码
     */
    private String featureCode;
    /**
     * 工具类型
     */
    private String toolType;
    /**
     * 账号信息
     */
    private String accountInformation;
    /**
     * 手机版本
     */
    private String softwareVersion;
    /**
     * 手机品牌
     */
    private String deviceBrand;
    /**
     * 品牌型号
     */
    private String deviceModel;
    /**
     * 工具版本号
     */
    private String toolVersion;
    /**
     * 鉴权方案版本号
     */
    private String projectVersion;//手机版本
    /**
     * 工单 ID
     */
    @TableField("work_order_id")
    private String workOrderID;
    private Date createTime;//创建时间
    private Date updateTime;//创建时间

}
