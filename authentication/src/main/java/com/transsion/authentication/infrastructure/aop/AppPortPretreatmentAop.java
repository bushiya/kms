package com.transsion.authentication.infrastructure.aop;

import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.constants.RequestHeadConst;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.utils.SHAUtils;
import com.transsion.authentication.module.auth.bean.req.DerivedKeyReq;
import com.transsion.authentication.module.auth.bean.req.SafeChannelReq;
import com.transsion.authentication.module.auth.repository.init.DeviceAppContext;
import com.transsion.authentication.module.sys.repository.dao.CertBlacklistDao;
import com.transsion.authentication.module.sys.repository.dao.ConfigInfoDao;
import com.transsion.authentication.module.sys.repository.entity.CertBlacklistEntity;
import com.transsion.authentication.module.sys.repository.entity.ConfigInfo;
import com.transsion.authentication.module.sys.repository.entity.DeviceAppEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 客户端端口前置处理
 * @Author jiakang.chen
 * @Date 2023/6/29
 */
@Slf4j
@Aspect
@Component
public class AppPortPretreatmentAop {

    @Autowired
    HttpServletRequest request;

    @Autowired
    CertBlacklistDao certBlacklistDao;

    @Autowired
    ConfigInfoDao configInfoDao;

    @Autowired
    DeviceAppContext deviceAppContext;

    @Pointcut("@annotation(com.transsion.authentication.infrastructure.annotation.AppPort)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object doAfter(ProceedingJoinPoint point) throws Throwable {
        // 获取请求参数
        Object[] args = point.getArgs();
        SafeChannelReq req1 = null;
        DerivedKeyReq req2 = null;
        for (Object arg : args) {
            if (arg instanceof SafeChannelReq) {
                req1 = (SafeChannelReq) arg;
            } else if (arg instanceof DerivedKeyReq) {
                req2 = (DerivedKeyReq) arg;
            }
        }
        String appId = request.getHeader(RequestHeadConst.APPID);
        String pkgName = request.getHeader(RequestHeadConst.PKG_NAME);
        String pkgSign = request.getHeader(RequestHeadConst.PKG_SIGN);
        log.info("请求路径：{},appId:{},pkgName:{},pkgSign:{},请求参数：{}", request.getRequestURI(), appId, pkgName, pkgSign, args);
        String deviceModel = request.getHeader(RequestHeadConst.DEVICE_MODEL);
        if (StringUtils.isAnyEmpty(appId, deviceModel, pkgName, pkgSign)) {
            throw new CustomException(NetCodeEnum.HEADERS_VERIFY_FAIL);
        }
        // 查询业务方是否已接入
        DeviceAppEntity deviceAppEntity = deviceAppContext.getDeviceApp(appId);
        // 判别业务方包名,签名是否一致
        if (!deviceAppEntity.getAppPackage().equals(pkgName)) {
            throw new CustomException(NetCodeEnum.APP_IN_USER_ERROR);
        }
        // 判断签名是否一致
        String[] signs = deviceAppEntity.getAppSign().split(",");
        boolean a = true;
        for (String sign : signs) {
            if (sign.equals(pkgSign)) {
                a = false;
                break;
            }
        }
        if (a) {
            throw new CustomException(NetCodeEnum.APP_IN_USER_ERROR);
        }
        if (ObjectUtils.isEmpty(deviceAppEntity)) {
            throw new CustomException(NetCodeEnum.APP_IN_USER_ERROR);
        }
        // 机型黑名单
        List<ConfigInfo> configInfos = configInfoDao.selectDeviceModelBlackList();
        if (ObjectUtils.isNotEmpty(configInfos)) {
            for (ConfigInfo configInfo : configInfos) {
                if (configInfo.getValue().equals(deviceModel)) {
                    throw new CustomException(NetCodeEnum.CLOUD_CLOSE_ERROR);
                }
            }
        }
        // 可信通道的单独处理逻辑
        if (ObjectUtils.isNotEmpty(req1)) {
            // 证书 sha256 值
            String certSha256 = SHAUtils.getSHA256(req1.getDeviceCert());
            // 查询是否存在证书黑名单中
            CertBlacklistEntity certBlacklistEntity = certBlacklistDao.selectByCert(certSha256);
            if (ObjectUtils.isNotEmpty(certBlacklistEntity)) {
                throw new CustomException(NetCodeEnum.CERT_ERROR);
            }
        }
        return point.proceed();
    }
}
