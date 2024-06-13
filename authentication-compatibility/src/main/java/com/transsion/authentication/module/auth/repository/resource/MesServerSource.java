package com.transsion.authentication.module.auth.repository.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@Data
@Component
@ConfigurationProperties("mes")
public class MesServerSource {
    private String getCertUrl;
    private String getStorageKeyUrl;
}
