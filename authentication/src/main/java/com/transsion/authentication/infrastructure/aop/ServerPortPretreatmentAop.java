package com.transsion.authentication.infrastructure.aop;

import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.constants.RequestHeadConst;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.utils.IPUtil;
import com.transsion.authentication.module.auth.repository.current.CurrentThreadApp;
import com.transsion.authentication.module.auth.repository.init.DeviceAppContext;
import com.transsion.authentication.module.sys.repository.entity.DeviceAppEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 服务端接口前置处理
 * @Author jiakang.chen
 * @Date 2023/6/29
 */
@Slf4j
@Aspect
@Component
public class ServerPortPretreatmentAop {
    @Autowired
    HttpServletRequest request;

    @Autowired
    DeviceAppContext deviceAppContext;

    @Pointcut("@annotation(com.transsion.authentication.infrastructure.annotation.ServerPort)")
    public void pointcut() {
    }

    /**
     * 将返回结果加上验签信息
     */
    @Around(value = "pointcut()")
    public Object doAfter(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        String appId = request.getHeader(RequestHeadConst.APPID);
        DeviceAppEntity appData = deviceAppContext.getDeviceApp(appId);
        if (ObjectUtils.isEmpty(appData)) {
            throw new CustomException(NetCodeEnum.APP_IN_USER_ERROR);
        }
//        log.info("本次请求业务方信息为：{}", appData);
        // IP 校验
        String ip = IPUtil.getIpAddr(request);
        log.info("请求路径：{},请求IP为：{},参数列表:{}", request.getRequestURI(), ip, args);
        String[] appIPs = appData.getIp().split(",");
        boolean ipTrue = true;
        for (int i = 0; i < appIPs.length; i++) {
            if (appIPs[i].equals(ip)) {
                ipTrue = false;
            }
        }
        if (ipTrue) {
            throw new CustomException(NetCodeEnum.IP_ERROR);
        }
        CurrentThreadApp.setCurrentUser(appData);
        return point.proceed();
    }
}
