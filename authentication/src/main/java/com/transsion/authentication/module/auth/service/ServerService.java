package com.transsion.authentication.module.auth.service;

import com.transsion.authentication.module.auth.bean.req.ServerInitReq;
import com.transsion.authentication.module.auth.bean.resp.ServerInitResp;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/11
 */
public interface ServerService {
    /**
     * 查询业务方通信Key
     *
     * @param appId     业务方 appId
     * @param serverTag 业务方集群机器标识
     * @return
     */
    String selectKey(String appId, String serverTag) throws Exception;

    /**
     * 业务方服务器初始化KMS 安全通道
     *
     * @param appId 业务方 appId
     * @param req   业务方验证身份信息
     * @return
     * @throws Exception
     */
    ServerInitResp init(String appId, ServerInitReq req) throws Exception;
}
