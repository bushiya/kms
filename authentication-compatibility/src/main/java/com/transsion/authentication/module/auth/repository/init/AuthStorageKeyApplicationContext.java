package com.transsion.authentication.module.auth.repository.init;

import com.transsion.authentication.module.auth.repository.resource.RootNumberResource;
import com.transsion.authentication.module.auth.service.MesAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Slf4j
@Component
public class AuthStorageKeyApplicationContext implements ApplicationContextAware {

    @Autowired
    MesAuthService mesAuthService;

    @Autowired
    RootNumberResource rootNumberResource;

    /**
     * 存储密钥
     */
    private static String authStorageKey = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // 调用MES服务获取服务存储密钥
        authStorageKey = mesAuthService.getStorageKey(rootNumberResource.getRootNumber()).substring(0, 16);
        log.info("服务存储密钥获取:{}", authStorageKey);
//        authStorageKey = rootNumberResource.getRootNumber();
        log.info("服务存储密钥获取完成");
    }

    /**
     * 获取存储密钥
     *
     * @return
     */
    public String getStorageKey() {
        assert !StringUtils.isBlank(authStorageKey) : "初始化服务存储密钥失败";
        return authStorageKey;
    }

}
