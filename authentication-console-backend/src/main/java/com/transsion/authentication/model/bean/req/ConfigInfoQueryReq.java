package com.transsion.authentication.model.bean.req;

import com.transsion.authentication.model.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 请求：黑名单配置查询实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigInfoQueryReq extends BaseEntity {

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
