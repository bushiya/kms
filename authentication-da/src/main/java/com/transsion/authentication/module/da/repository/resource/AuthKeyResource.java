package com.transsion.authentication.module.da.repository.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Description: 服务通信安全 非对称密钥
 * @Author jiakang.chen
 * @Date 2023/5/24
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("server.auth.key")
public class AuthKeyResource {
    //服务公钥
    private String serverPublicKey;
    //服务私钥
    private String serverPrivateKey;
    //服务私钥
    private String serverPrivateKeyV3;
    //pc公钥
    private String pcPublicKey;
    //手机根私钥
    private String phonePrivateKey;
    //pc公钥
    private String pcPublicKeyV3;
}
