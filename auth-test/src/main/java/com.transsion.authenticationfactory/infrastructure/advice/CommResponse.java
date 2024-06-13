package com.transsion.authenticationfactory.infrastructure.advice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.transsion.authenticationfactory.infrastructure.constants.NetCodeEnum;
import lombok.Data;

/**
 * @Description: 公共返回体
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@Data
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

    public static CommResponse errorResult(NetCodeEnum netCodeEnum) {
        CommResponse commResponse = new CommResponse();
        commResponse.setCode(netCodeEnum.getCode());
        commResponse.setMessage(netCodeEnum.getMessageKey());
        return commResponse;
    }

    public static CommResponse errorResult(NetCodeEnum netCodeEnum, Object data) {
        CommResponse commResponse = new CommResponse();
        commResponse.setCode(netCodeEnum.getCode());
        commResponse.setMessage(netCodeEnum.getMessageKey());
        commResponse.setData(data);
        return commResponse;
    }

    public static CommResponse success() {
        CommResponse commResponse = new CommResponse();
        commResponse.setCode(NetCodeEnum.SUCCESS.getCode());
        commResponse.setMessage(NetCodeEnum.SUCCESS.getMessageKey());
        return commResponse;
    }

    public static CommResponse success(Object data) {
        CommResponse commResponse = new CommResponse();
        commResponse.setCode(NetCodeEnum.SUCCESS.getCode());
        commResponse.setMessage(NetCodeEnum.SUCCESS.getMessageKey());
        commResponse.setData(data);
        return commResponse;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return code == NetCodeEnum.SUCCESS.getCode();
    }
}
