package com.transsion.daconsole.module.platform.repository.entity;

import lombok.Data;

import java.util.Map;

@Data
public class PlatformEntity {
    private Integer code;
    private String msg;
    private Map<String, Object> data;
}
