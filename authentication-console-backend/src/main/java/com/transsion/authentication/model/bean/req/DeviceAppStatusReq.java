package com.transsion.authentication.model.bean.req;

import com.transsion.authentication.model.bean.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 请求：设备应用变更状态实体信息
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Data
public class DeviceAppStatusReq {

    /**
     * 业务方ID
     */
    @NotBlank(message = "应用ID不能为空")
    private String appId;

    /**
     * 起停用 : 1.禁用  2.启用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}
