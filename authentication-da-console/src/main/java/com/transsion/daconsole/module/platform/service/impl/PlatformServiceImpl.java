package com.transsion.daconsole.module.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.transsion.daconsole.infrastructure.admin.AdminProperties;
import com.transsion.daconsole.infrastructure.admin.JwtOperator;
import com.transsion.daconsole.module.platform.repository.entity.PlatformEntity;
import com.transsion.daconsole.module.platform.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AdminProperties adminProperties;

    @Autowired
    JwtOperator jwtOperator;

    public String toSgin(String sign) {
        //Base64加密
        String encode = Base64.getEncoder().encodeToString(sign.getBytes());
        //在经过Md5加密
        String s = DigestUtils.md5DigestAsHex(encode.getBytes());
        return s;
    }

    public Map<String, Object> get(String token) {
        //获取当前时间戳
        long times = System.currentTimeMillis();
        //获取配置文件中的项目ID
        String appId = adminProperties.getAppId().toString();
        //获取项目密钥
        String appSecret = adminProperties.getAppSecret();
        //初始化密钥
        String sign = "appId=" + appId + "&appSecret=" + appSecret + "&times=" + times;
        //加密密钥
        String s = this.toSgin(sign);
        //获取聚合平台地址
        String url = adminProperties.getUrl();
        //设置参数
        MultiValueMap map = new LinkedMultiValueMap<String, Object>();
        map.add("appId", appId);
        map.add("token", token);
        map.add("times", times);
        map.add("sign", s);
        //远程调用并返回用户信息
        String s1 = restTemplate.postForObject(url, map, String.class);
        PlatformEntity responseEntity = JSON.parseObject(s1, PlatformEntity.class);
        HashMap<String, Object> response = null;
        if (responseEntity.getCode() == 1001) {
            return null;
        } else {
            Object userInfo = responseEntity.getData().get("userInfo");
            String userData = jwtOperator.getJwtToken("userData", s1);
            response = new HashMap<String, Object>();
            response.put("userInfo", userInfo);
            response.put("token", userData);
        }
        return response;
    }

    @Override
    public String refreshToken(HttpServletRequest request) {
        //获取请求头中的旧token
        String token = request.getHeader("token");
        //获取用户的信息
        Object userData = jwtOperator.parseToken(token).get("userData");
        return jwtOperator.getJwtToken("userData", JSON.toJSONString(userData));
    }


}
