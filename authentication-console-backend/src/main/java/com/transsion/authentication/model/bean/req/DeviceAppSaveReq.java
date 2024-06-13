package com.transsion.authentication.model.bean.req;

import com.transsion.authentication.model.bean.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 请求：设备应用保存实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class DeviceAppSaveReq extends BaseEntity {

    /**
     * 业务方ID
     */
    @NotBlank(message = "应用ID不能为空")
    private String appId;

    /**
     * 业务方名称
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName;

    /**
     * 业务方包名
     */
    @NotBlank(message = "应用包名不能为空")
    private String appPackage;

    /**
     * 业务方签名
     */
    @NotBlank(message = "应用签名不能为空")
    private String appSign;

    /**
     * 业务方描述
     */
    @NotBlank(message = "应用描述不能为空")
    private String appDes;
}
