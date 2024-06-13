package com.transsion.authentication.infrastructure.job;

import com.transsion.authentication.module.auth.repository.dao.DeviceRootSecretKeyDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 设备通信密钥过期删除 Job
 * @Author jiakang.chen
 * @Date 2023/6/29
 */
public class DeviceSecretKeyPastDueDeleteJob {
    @Autowired
    DeviceRootSecretKeyDao rootSecretKeyDao;

}
