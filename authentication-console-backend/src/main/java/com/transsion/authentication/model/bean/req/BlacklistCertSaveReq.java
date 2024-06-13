package com.transsion.authentication.model.bean.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 请求：黑名单证书保存实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class BlacklistCertSaveReq {

    /**
     * 非法证书的sha256值
     */
    @NotBlank(message = "非法证书的sha256值不能为空")
    private String certValue;

    /**
     * 黑名单证书名称
     */
    @NotBlank(message = "黑名单证书名称不能为空")
    private String name;
}
