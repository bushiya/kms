package com.transsion.authentication.module.auth.controller;

import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.module.auth.bean.req.*;
import com.transsion.authentication.module.auth.bean.resp.EncodeMessageResp;
import com.transsion.authentication.module.auth.bean.resp.SafeChannelResp;
import com.transsion.authentication.module.auth.bean.resp.SafeChannelTransmitResp;
import com.transsion.authentication.module.auth.bean.resp.SignResp;
import com.transsion.authentication.module.auth.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@Slf4j
@RestController
@RequestMapping("device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    /**
     * 建立可信通道 1
     */
    @PostMapping("safe-channel")
    public CommResponse<SafeChannelResp> safeChannel(@RequestBody @Validated SafeChannelReq req) {
        return CommResponse.success(deviceService.safeChannel(req));
    }

    /**
     * 建立可信通道 2
     */
    @PostMapping("safe-channel-transmit")
    public CommResponse<SafeChannelTransmitResp> safeChannelTransmit(@RequestBody @Validated SafeChannelReq req) throws Exception {
        return CommResponse.success(deviceService.safeChannelTransmit(req));
    }

    /**
     * 加密消息
     */
    @PostMapping("encode-message")
    public CommResponse<EncodeMessageResp> encodeMessage(@RequestBody @Validated EncodeMessageReq req) throws Exception {
        return CommResponse.success(deviceService.encodeMessage(req));
    }

    /**
     * 解密消息
     */
    @PostMapping("decode-message")
    public CommResponse<String> decodeMessage(@RequestBody @Validated DecodeMessageReq req) throws Exception {
        return CommResponse.success(deviceService.decodeMessage(req));
    }

    /**
     * 签名消息
     */
    @PostMapping("sign-message")
    public CommResponse<SignResp> signMessage(@RequestBody @Validated SignMessageReq req) throws Exception {
        return CommResponse.success(deviceService.signMessage(req));
    }

    /**
     * 验签消息
     */
    @PostMapping("verify-sign")
    public CommResponse<Boolean> verifySign(@RequestBody @Validated VerifySignReq req) throws Exception {
        return CommResponse.success(deviceService.verifySign(req));
    }

    /**
     * 衍生密钥
     */
    @PostMapping("derived-key")
    public CommResponse<Object> derivedKey(@RequestBody @Validated DerivedKeyReq req) throws Exception {
        CommResponse<Object> success = CommResponse.success(deviceService.derivedKey(req));
        return success;
    }

}
