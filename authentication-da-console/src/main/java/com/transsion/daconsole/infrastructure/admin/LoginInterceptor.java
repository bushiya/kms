package com.transsion.daconsole.infrastructure.admin;

import com.alibaba.fastjson.JSON;
import com.transsion.daconsole.infrastructure.advice.CommResponse;
import com.transsion.daconsole.infrastructure.constants.NetCodeEnum;
import com.transsion.daconsole.module.platform.repository.entity.CurrentThreadUser;
import com.transsion.daconsole.module.platform.repository.entity.OperatorEntity;
import com.transsion.daconsole.module.platform.repository.entity.PlatformEntity;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    JwtOperator jwtOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求登陆地址,如果进行登陆操作则不拦截
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login/getToken")) {
            return true;
        } else {
            //获取请求头token
            String token = request.getHeader("token");
            //对token进行解密
            Claims claims = jwtOperator.parseToken(token);
            //如果解密失败，代表token失效或者被篡改
            if (claims == null) {
                response.setContentType("application/json");
                CommResponse error = CommResponse.errorResult(NetCodeEnum.LOGIN_OUT, "登录信息失效，请重新登录");
                String s = JSON.toJSONString(error);
                response.getOutputStream().write(s.getBytes());
                return false;
            } else {
                //jwt格式 用户信息 角色信息 路由列表
                Object userData = claims.get("userData");
                //用户信息 角色信息 路由列表
                PlatformEntity platformEntity = JSON.parseObject(userData.toString(), PlatformEntity.class);
                //提取用户信息
                OperatorEntity userInfo = JSON.parseObject(platformEntity.getData().get("userInfo").toString(), OperatorEntity.class);
                //将用户信息保存在当前线程
                CurrentThreadUser.setCurrentUser(userInfo);
                return true;
            }
        }
    }
}
