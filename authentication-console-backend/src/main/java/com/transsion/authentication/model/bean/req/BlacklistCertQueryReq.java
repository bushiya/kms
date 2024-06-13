package com.transsion.authentication.model.bean.req;

import com.transsion.authentication.model.bean.BaseEntity;
import lombok.Data;

/**
 * 请求：黑名单证书查询实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class BlacklistCertQueryReq extends BaseEntity {

    /**
     * 黑名单证书名称
     */
    private String name;
}
