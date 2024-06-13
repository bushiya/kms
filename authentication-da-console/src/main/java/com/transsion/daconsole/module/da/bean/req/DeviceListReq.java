package com.transsion.daconsole.module.da.bean.req;

import lombok.Data;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/10/19
 */
@Data
public class DeviceListReq {
    private Integer pageNo;
    private Integer pageSize;
    private String chipId;
    private String featureCode;
    private String scene;
    private String chipOriginalId;
    private String deviceBrand;
    private String deviceModel;
    private String toolVersion;
    private String workOrderID;
    private String softwareVersion;
}
