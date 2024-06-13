package com.transsion.authenticationsdk.infrastructure.advice;

/**
 * @Description: 公共返回体
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
public class CommResponse<T> {
    private int code; //0 成功，其他失败

    private String message;

    private T data;

    public CommResponse() {
    }

    public CommResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
