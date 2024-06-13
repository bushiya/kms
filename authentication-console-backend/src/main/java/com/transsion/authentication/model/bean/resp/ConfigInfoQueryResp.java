package com.transsion.authentication.model.bean.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 响应：黑名单机型配置信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigInfoQueryResp {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String des;

    /**
     * 起停用 : 1.禁用  2.启用
     */
    private Integer status;
}
