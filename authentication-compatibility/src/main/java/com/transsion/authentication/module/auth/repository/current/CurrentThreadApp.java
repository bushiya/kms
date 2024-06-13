package com.transsion.authentication.module.auth.repository.current;

import com.transsion.authentication.module.sys.repository.entity.DeviceAppEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/29
 */
@Slf4j
public class CurrentThreadApp {
    private static ThreadLocal<DeviceAppEntity> appData = new ThreadLocal<>();

    public static DeviceAppEntity getCurrentApp() {
        if (ObjectUtils.isEmpty(appData)) {
            log.info("当前线程业务方为空");
        }
        return appData.get();
    }

    public static void setCurrentUser(DeviceAppEntity app) {
        if (ObjectUtils.isEmpty(app)) {
            log.info("设置当前线程业务方");
            return;
        }
        appData.set(app);
    }
}
