package com.transsion.authentication.infrastructure.advice;

import com.transsion.authentication.infrastructure.enums.NetCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @author： nan.hu on 2022/8/9
 * =======================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommResponse<T> {
    private int code; //0 成功，其他失败

    private String message;

    private T data;

    public static CommResponse success(Object data) {
        CommResponse commResponse = new CommResponse();
        commResponse.setCode(NetCodeEnum.SUCCESS.getCode());
        commResponse.setMessage(NetCodeEnum.SUCCESS.getMessageKey());
        commResponse.setData(data);
        return commResponse;
    }

    public static CommResponse success() {
        CommResponse commResponse = new CommResponse();
        commResponse.setCode(NetCodeEnum.SUCCESS.getCode());
        commResponse.setMessage(NetCodeEnum.SUCCESS.getMessageKey());
        return commResponse;
    }
}
