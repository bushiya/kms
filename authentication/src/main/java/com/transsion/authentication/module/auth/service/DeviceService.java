package com.transsion.authentication.module.auth.service;

import com.transsion.authentication.module.auth.bean.req.*;
import com.transsion.authentication.module.auth.bean.resp.EncodeMessageResp;
import com.transsion.authentication.module.auth.bean.resp.SafeChannelResp;
import com.transsion.authentication.module.auth.bean.resp.SafeChannelTransmitResp;
import com.transsion.authentication.module.auth.bean.resp.SignResp;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/25
 */
public interface DeviceService {
    /**
     * 建立可信通道
     *
     * @param req
     * @return
     */
    SafeChannelResp safeChannel(SafeChannelReq req);

    /**
     * 建立可信通道 2
     *
     * @param req
     * @return
     */
    SafeChannelTransmitResp safeChannelTransmit(SafeChannelReq req) throws Exception;

    /**
     * 加密消息
     *
     * @param req
     * @return
     */
    EncodeMessageResp encodeMessage(EncodeMessageReq req) throws Exception;

    /**
     * 解密消息
     *
     * @param req
     * @return
     */
    String decodeMessage(DecodeMessageReq req) throws Exception;

    /**
     * 签名消息
     *
     * @param req
     * @return
     */
    SignResp signMessage(SignMessageReq req) throws Exception;

    /**
     * 验签消息
     *
     * @param req
     * @return
     */
    Boolean verifySign(VerifySignReq req) throws Exception;

    /**
     * 衍生密钥
     *
     * @param req
     * @return
     */
    Object derivedKey(DerivedKeyReq req) throws Exception;
}
