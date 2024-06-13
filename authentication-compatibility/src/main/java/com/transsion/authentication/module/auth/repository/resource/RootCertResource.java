package com.transsion.authentication.module.auth.repository.resource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@Data
@Component
public class RootCertResource {
    /**
     * 根证书 字符串
     */
    @Value("${server.root.cert}")
    private String rootCert;

}
