package com.transsion.daconsole.module.platform.controller;

import com.transsion.daconsole.infrastructure.advice.CommResponse;
import com.transsion.daconsole.infrastructure.constants.NetCodeEnum;
import com.transsion.daconsole.module.platform.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("login")
public class PlatformController {
    @Autowired
    PlatformService platformService;

    @RequestMapping("getToken")
    public CommResponse get(String token) {
        Map<String, Object> s = platformService.get(token);
        return s != null ? CommResponse.success(s) : CommResponse.errorResult(NetCodeEnum.LOGIN_OUT, "二维码已失效，请重新登录");
    }

    @RequestMapping("refreshToken")
    public CommResponse refreshToken(@Autowired HttpServletRequest request) {
        return CommResponse.success(platformService.refreshToken(request));
    }
}
