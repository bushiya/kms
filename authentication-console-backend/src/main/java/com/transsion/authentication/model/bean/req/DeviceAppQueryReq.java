package com.transsion.authentication.model.bean.req;

import com.transsion.authentication.model.bean.BaseEntity;
import lombok.Data;

/**
 * 请求：设备应用查询实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class DeviceAppQueryReq extends BaseEntity {

    /**
     * 业务方名称
     */
    private String appName;

    /**
     * 业务方包名
     */
    private String appPackage;

}
