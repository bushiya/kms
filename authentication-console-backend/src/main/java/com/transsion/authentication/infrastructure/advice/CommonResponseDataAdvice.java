package com.transsion.authentication.infrastructure.advice;

import com.transsion.authentication.infrastructure.annotation.AnnotationIgnoreResponseAdvice;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.enums.NetCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.transsion.authentication.infrastructure.enums.NetCodeEnum.SUCCESS;

/**
 * @Description:
 * @author： nan.hu on 2022/8/9
 * =======================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
@RestControllerAdvice
@Slf4j
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class converterType) {
        if (methodParameter.getDeclaringClass().isAnnotationPresent(AnnotationIgnoreResponseAdvice.class)) {
            return false;
        }
        if (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(AnnotationIgnoreResponseAdvice.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof CommResponse) {
            return body;
        }
        CommResponse commResponse = new CommResponse<>(SUCCESS.getCode(), SUCCESS.getMessageKey(), body);
        if (body instanceof String) {
            return body;
//            ObjectMapper om = new ObjectMapper();
//            try {
//                return om.writeValueAsString(commResponse);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
        }
        return commResponse;
    }

  /*  @ExceptionHandler(value = Exception.class)
    public String handlerGlobeException(HttpServletRequest request, Exception exception) {

        return "status" + "=" + NetCodeEnum.SYSTEM_ERROR.getCode() +
                "&" + "message" + "=" + NetCodeEnum.SYSTEM_ERROR.getMessageKey();
    }*/

    /**
     * 接收Exception异常
     *
     * @param request   HTTP 请求
     * @param exception 异常本身
     * @return 返回 系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public CommResponse handlerGlobeException(HttpServletRequest request, Exception exception) {
        log.info("url {} \n", request.getRequestURL(), exception);
        return new CommResponse<>(NetCodeEnum.SYSTEM_ERROR.getCode(), NetCodeEnum.SYSTEM_ERROR.getMessageKey(), null);
    }


    @ExceptionHandler(value = CustomException.class)
    public String handlerBaseKnowException(CustomException e) {
        StringBuilder builder = new StringBuilder();
        builder.append("status").append("=").append(e.getCode());
        if (StringUtils.isNotEmpty(e.getMessage())) {
            builder.append("&").append("message").append("=").append(e.getMessage());
        }
        if (e.getData() != null) {
            builder.append("&").append("data").append(e.getData());
        }
        return builder.toString();
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public String handlerArgumentNotValidException(MethodArgumentNotValidException e) {
        return "status" + "=" + NetCodeEnum.PARAM_VERIFY_FAIL.getCode() +
                "&" + "message" + "=" + NetCodeEnum.PARAM_VERIFY_FAIL.getMessageKey();
    }
}
