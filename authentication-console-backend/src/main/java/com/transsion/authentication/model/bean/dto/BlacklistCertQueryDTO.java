package com.transsion.authentication.model.bean.dto;

import lombok.Data;

import java.util.Date;

/**
 * 转换层：黑名单证书查询实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class BlacklistCertQueryDTO {

    /**
     * 主键
     */
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
