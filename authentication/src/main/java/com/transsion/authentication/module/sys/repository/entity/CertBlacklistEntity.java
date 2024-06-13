package com.transsion.authentication.module.sys.repository.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑名单证书信息
 *
 * @author jiakang.chen
 * @since 2023-06-27
 */
@Data
@TableName("blacklist_cert")
public class CertBlacklistEntity implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 非法证书的sha256值
     */
    private String certValue;

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
