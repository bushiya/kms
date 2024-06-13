package com.transsion.testtool.infrastructure.exception;

import com.transsion.testtool.infrastructure.constants.NetCodeEnum;

/**
 * @Description: 数据管理服务自定义异常
 * @Author jiakang.chen
 * @Date 2023/2/8
 */
public class CustomException extends RuntimeException {
    private int code = 500;
    private String message;

    public CustomException(String msg) {
        super(msg);
        this.message = msg;
    }

    public CustomException(String msg, Throwable e) {
        super(msg, e);
        this.message = msg;
    }

    public CustomException(String msg, int code) {
        super(msg);
        this.message = msg;
        this.code = code;
    }

    public CustomException(String msg, int code, String message) {
        super(msg);
        this.code = code;
        this.message = message;
    }

    public CustomException(String msg, int code, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    public CustomException(NetCodeEnum netCode) {
        super(netCode.getMessageKey());
        this.message = netCode.getMessageKey();
        this.code = netCode.getCode();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
