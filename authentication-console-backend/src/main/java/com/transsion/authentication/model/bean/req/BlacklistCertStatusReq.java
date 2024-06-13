package com.transsion.authentication.model.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 请求：黑名单证书变更状态实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class BlacklistCertStatusReq {

    /**
     * 主键
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 起停用 : 1.禁用  2.启用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
