package com.transsion.authentication.module.auth.controller;

import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.infrastructure.constants.RequestHeadConst;
import com.transsion.authentication.module.auth.bean.req.ServerInitReq;
import com.transsion.authentication.module.auth.bean.resp.ServerInitResp;
import com.transsion.authentication.module.auth.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 业务方服务器初始化时操作
 * @Author jiakang.chen
 * @Date 2023/7/7
 */
@RestController
@RequestMapping("server")
public class ServerController {

    @Autowired
    ServerService service;


    @RequestMapping("init")
    public CommResponse<ServerInitResp> initServer(@RequestHeader(RequestHeadConst.APPID) String appId,
                                                   @RequestBody @Validated ServerInitReq req) throws Exception {
        return CommResponse.success(service.init(appId, req));
    }
}
