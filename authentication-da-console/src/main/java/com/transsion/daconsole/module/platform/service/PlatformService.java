package com.transsion.daconsole.module.platform.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PlatformService {
    Map<String, Object> get(String token);

    String refreshToken(HttpServletRequest request);
}
