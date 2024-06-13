package com.transsion.authentication.infrastructure.constants;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/27
 */
public class RedisKeyPreConst {
    /**
     * 可信通道建立 - 存储返回给客户端的随机数用于 生成 通信密钥校验值
     */
    public static final String SAFE_CHANNEL_ = "safe-channel:random-key:";
}
