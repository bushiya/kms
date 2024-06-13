package com.transsion.authentication.infrastructure.advice;

import com.transsion.authentication.infrastructure.annotation.AnnotationIgnoreResponseAdvice;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Description: 异常处理类
 * @Author jiakang.chen
 * @Date 2023/6/19
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

    /**
     * 返回结果之前
     *
     * @param body                  返回体
     * @param returnType            返回类型
     * @param selectedContentType   选定的内容类型
     * @param selectedConverterType 选定转换器类型
     * @param request               HTTP 请求
     * @param response              HTTP 相应
     * @return 接口响应结果
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        CommResponse commResponse;
        if (body instanceof CommResponse) {
            commResponse = (CommResponse) body;
        } else {
            commResponse = CommResponse.success(body);
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return commResponse;
        }
        HttpServletRequest httpServletRequest = attributes.getRequest();
        return commResponse;
    }

    /**
     * 自定义异常处理器
     *
     * @param request HTTP 请求
     * @param e       自定义异常本身
     * @return 返回 自定义异常内容
     */
    @ExceptionHandler(value = {CustomException.class})
    public CommResponse handlerDecryptException(HttpServletRequest request, CustomException e) {
        log.info("url {} , {}", request.getRequestURL(), e.getMsg());
        return new CommResponse<>(e.getCode(), e.getMsg(), null);
    }

    /**
     * 接收Exception异常
     *
     * @param request HTTP 请求
     * @param e       异常本身
     * @return 返回 系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public CommResponse handlerGlobeException(HttpServletRequest request, Exception e) {
        log.info("url {} , {}", request.getRequestURL(), e.getMessage());
        return new CommResponse<>(NetCodeEnum.SYSTEM_ERROR.getCode(), NetCodeEnum.SYSTEM_ERROR.getMessageKey(), null);
    }

    /**
     * 方法参数丢失异常
     *
     * @param request HTTP 请求
     * @param e       异常本身
     * @return 返回 400 参数异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public CommResponse handlerArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.info("url {} , {}", request.getRequestURL(), e.getMessage());
        return new CommResponse<>(NetCodeEnum.PARAM_VERIFY_FAIL.getCode(), NetCodeEnum.PARAM_VERIFY_FAIL.getMessageKey(), null);
    }

    /**
     * 请求头丢失异常
     *
     * @param request HTTP 请求
     * @param e       异常本身
     * @return 返回 400 参数异常
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MissingRequestValueException.class})
    public CommResponse handlerHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.info("url {} , {}", request.getRequestURL(), e.getMessage());
        return new CommResponse<>(NetCodeEnum.HEADERS_VERIFY_FAIL.getCode(), NetCodeEnum.HEADERS_VERIFY_FAIL.getMessageKey(), null);
    }


}
