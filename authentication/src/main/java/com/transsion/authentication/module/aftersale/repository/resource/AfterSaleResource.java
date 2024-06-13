package com.transsion.authentication.module.aftersale.repository.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 售后认证资源
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
@Data
@Component
@ConfigurationProperties("server.auth.key")
public class AfterSaleResource {
    //服务公钥
    private String serverPublicKey;
    //服务私钥
    private String serverPrivateKey;
    //pc公钥
    private String pcPublicKey;
}
