package com.transsion.authentication.module.da.repository.resource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Description: 服务校验 第三方平台 参数
 * @Author jiakang.chen
 * @Date 2023/6/10
 */
@Data
@Component
@RefreshScope
public class AuthResource {
    /**
     * 售后特征码 验证特征码 URL
     */
    @Value("${auth.server.afterSave.url}")
    private String afterSaveUrl;
    /**
     * 售后特征码 验证特征码 URL
     */
    @Value("${auth.server.afterSave.publicKey}")
    private String publicKey;

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

    /**
     * 粉丝 账号 URL
     */
    @Value("${auth.server.fans.url}")
    private String fansUrl;

}
