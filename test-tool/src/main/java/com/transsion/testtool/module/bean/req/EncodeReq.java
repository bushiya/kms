package com.transsion.testtool.module.bean.req;

import lombok.Data;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/8/9
 */
@Data
public class EncodeReq {
    private String sessionId;
    private String scene;
    private String data;
    private String sign;
    private String keyIndex;

    private String algo;
    private String dPub;
    private String kLoc;
    private String sc;
}
