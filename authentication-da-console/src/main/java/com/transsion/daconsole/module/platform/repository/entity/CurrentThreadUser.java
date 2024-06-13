package com.transsion.daconsole.module.platform.repository.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CurrentThreadUser {
    private static ThreadLocal<OperatorEntity> userData = new ThreadLocal<>();

    public static OperatorEntity getCurrentUser() {
        if (ObjectUtils.isEmpty(userData)) {
            log.info("当前线程私钥对象获取时为空");
        }
        return userData.get();
    }

    public static void setCurrentUser(OperatorEntity user) {
        if (ObjectUtils.isEmpty(user)) {
            log.info("设置当前线程");
            return;
        }
        userData.set(user);
    }
}
