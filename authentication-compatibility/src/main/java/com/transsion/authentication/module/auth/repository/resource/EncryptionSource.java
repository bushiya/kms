package com.transsion.authentication.module.auth.repository.resource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/8/22
 */
@Data
@Component
public class EncryptionSource {
    @Value("${server.key.path}")
    private String filePath;
}
