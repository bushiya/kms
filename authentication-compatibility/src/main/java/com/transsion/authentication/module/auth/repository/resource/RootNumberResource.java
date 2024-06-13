package com.transsion.authentication.module.auth.repository.resource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/25
 */
@Data
@Component
public class RootNumberResource {
    @Value("${auth.rootNumber}")
    private String rootNumber;
}
