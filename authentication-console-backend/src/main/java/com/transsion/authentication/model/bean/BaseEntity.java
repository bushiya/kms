package com.transsion.authentication.model.bean;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 公共类
 *
 * @author yuandonghai
 * @since 2023-02-13
 */
@Data
public class BaseEntity {

    /**
     * 当前页数
     */
    private int pageNo;

    /**
     * 当前条数
     */
    private int pageSize = 10;

    /**
     * IP
     */
    @NotEmpty(message = "IP不能为空")
    private String ip;

    /**
     * app公钥
     */
    @NotEmpty(message = "APP公钥不能为空")
    private String appPub;

}
