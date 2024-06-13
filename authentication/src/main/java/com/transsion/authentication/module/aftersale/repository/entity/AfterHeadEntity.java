package com.transsion.authentication.module.aftersale.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 售后认证请求头
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterHeadEntity {
    private String toolVersion;
    private String chipId;
    private String imei;
    private String deviceModel;
    private String softwareVersion;
    private String pointId;
}
