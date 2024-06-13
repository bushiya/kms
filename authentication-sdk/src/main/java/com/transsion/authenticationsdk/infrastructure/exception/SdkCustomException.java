package com.transsion.authenticationsdk.infrastructure.exception;

import com.transsion.authenticationsdk.infrastructure.constants.NetCodeEnum;

/**
 * @Description: SDK 异常
 * @Author jiakang.chen
 * @Date 2023/7/12
 */
public class SdkCustomException extends RuntimeException {
    private int code;
    private String massage;

    public SdkCustomException(String massage) {
        super(massage);
        this.massage = massage;
    }
    public SdkCustomException(int code,String massage) {
        super(massage);
        this.code = code;
        this.massage = massage;
    }

    public SdkCustomException(NetCodeEnum netCodeEnum) {
        this.code = netCodeEnum.getCode();
        this.massage = netCodeEnum.getMessageKey();
    }

    public int getCode() {
        return code;
    }

    public String getMassage() {
        return massage;
    }

    @Override
    public String toString() {
        return massage;
    }
}
