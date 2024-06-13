package com.transsion.testtool.module;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/9/7
 */
@Data
@Component
@ConfigurationProperties("kms")
public class KmsProperties {
    private String appId;
    private String url;
    private String compatibilityUrl;
    private String sdkPublicKey;
    private String sdkPrivateKey;
    private String authPublicKey;
}
