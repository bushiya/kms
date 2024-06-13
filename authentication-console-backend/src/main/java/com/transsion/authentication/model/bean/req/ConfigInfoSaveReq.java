package com.transsion.authentication.model.bean.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 请求：黑名单配置保存实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigInfoSaveReq {

    /**
     * key
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空")
    private String value;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String des;

}
