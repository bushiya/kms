package com.transsion.authentication.module.aftersale.repository.resource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description: 服务校验 第三方平台 参数
 * @Author jiakang.chen
 * @Date 2023/6/10
 */
@Data
@Component
public class AuthResource {
    /**
     * 售后特征码 验证特征码 URL
     */
    @Value("${auth.server.afterSave.url}")
    private String afterSaveUrl;

    /**
     * 研发 OA账号 URL
     */
    @Value("${auth.server.research.url}")
    private String researchUrl;

    /**
     * 研发 OA账号 AppID
     */
    @Value("${auth.server.research.appId}")
    private String researchAppId;

}
