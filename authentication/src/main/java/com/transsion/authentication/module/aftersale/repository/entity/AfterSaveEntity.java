package com.transsion.authentication.module.aftersale.repository.entity;

import lombok.Data;

/**
 * @Description: 售后认证参数
 * @Author jiakang.chen
 * @Date 2023/6/10
 */
@Data
public class AfterSaveEntity {
    private String id;
    private String branch;
    private String branchCode;
    private String country;
    private String countryCode;
    private String featureCode;
    private String functions;
    private String location;
    private String region;
    private String expireTime;
    private String lastUseTime;
}
