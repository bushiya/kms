package com.transsion.authentication.infrastructure.exception;

import com.transsion.authentication.infrastructure.enums.NetCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @author： nan.hu on 2022/8/13
 * =======================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */

@Getter
@Setter
public class CustomException extends RuntimeException {
    private int code;
    private Object data;

    public CustomException() {
        this.code = 500;
    }

    public CustomException(int code) {
        this.code = code;
    }

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public CustomException(NetCodeEnum codeEnum) {
        this.code = codeEnum.getCode();
    }

    public CustomException(NetCodeEnum codeEnum, Object data) {
        this.code = codeEnum.getCode();
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseKnowException [code = " + code + " , message = " + getMessage() + "]";
    }
}
