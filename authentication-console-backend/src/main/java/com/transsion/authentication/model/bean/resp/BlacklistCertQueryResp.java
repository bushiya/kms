package com.transsion.authentication.model.bean.resp;

import lombok.Data;

/**
 * 响应：黑名单证书查询实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class BlacklistCertQueryResp {

    /**
     * 主键
     */
    private Long id;

    /**
     * 非法证书的sha256值
     */
    private String certValue;

    /**
     * 起停用 : 1.禁用  2.启用
     */
    private Integer status;
}
