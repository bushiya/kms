package com.transsion.authentication.module.da.controller;

import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.da.controller.req.AuthRecordReq;
import com.transsion.authentication.module.da.controller.req.DaReq;
import com.transsion.authentication.module.da.controller.rsp.DaResp;
import com.transsion.authentication.module.da.controller.rsp.DaZrResp;
import com.transsion.authentication.module.da.service.DaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/device")
public class DaController {

    @Autowired
    private DaService daService;

//    @PostMapping("/mtk-download-verify")
//    public CommResponse mtkDownloadVerify(@RequestBody DaReq da) throws Exception {
//        if (StringUtils.isAnyEmpty(da.getVerifyData(), da.getEncodeRandomNumber(), da.getSign()) || ObjectUtils.isEmpty(da.getScene())) {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
//        return CommResponse.success(daService.mtkDownloadVerify(da));
//    }
//
//    @PostMapping("/sprd-fdl-verify")
//    public CommResponse sprdFdlVerify(@RequestBody DaReq da) throws Exception {
//        if (StringUtils.isAnyEmpty(da.getVerifyData(), da.getEncodeRandomNumber(), da.getSign()) || ObjectUtils.isEmpty(da.getScene())) {
//            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
//        }
//        return CommResponse.success(daService.sprdFdlVerify(da));
//    }

    @PostMapping("/mtk-download-verify-v2")
    public CommResponse mtkDownloadVerifyV2(@RequestBody DaReq da, @RequestHeader("Scene") Integer scene) throws Exception {
        da.setScene(scene);
        if (StringUtils.isAnyEmpty(da.getVerifyData(), da.getEncodeRandomNumber(), da.getSign()) || ObjectUtils.isEmpty(da.getScene())) {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        DaResp daResp = daService.mtkDownloadVerifyV2(da);
        return CommResponse.success(daResp);
    }

    @PostMapping("/sprd-fdl-verify-v2")
    public CommResponse sprdFdlVerifyV2(@RequestBody DaReq da, @RequestHeader("Scene") Integer scene) throws Exception {
        da.setScene(scene);
        if (StringUtils.isAnyEmpty(da.getVerifyData(), da.getEncodeRandomNumber(), da.getSign()) || ObjectUtils.isEmpty(da.getScene())) {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        DaZrResp daZrResp = daService.sprdFdlVerifyV2(da);
        return CommResponse.success(daZrResp);
    }


    @PostMapping("/mtk-download-verify-v3")
    public CommResponse mtkDownloadVerifyV3(@RequestBody DaReq da, @RequestHeader("Scene") Integer scene) throws Exception {
        da.setScene(scene);
        if (StringUtils.isAnyEmpty(da.getVerifyData(), da.getEncodeRandomNumber(), da.getSign()) || ObjectUtils.isEmpty(da.getScene())) {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        DaResp daResp = daService.mtkDownloadVerifyV3(da);
        return CommResponse.success(daResp);
    }

    @PostMapping("/sprd-fdl-verify-v3")
    public CommResponse sprdFdlVerifyV3(@RequestBody DaReq da, @RequestHeader("Scene") Integer scene) throws Exception {
        da.setScene(scene);
        if (StringUtils.isAnyEmpty(da.getVerifyData(), da.getEncodeRandomNumber(), da.getSign()) || ObjectUtils.isEmpty(da.getScene())) {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        DaZrResp daZrResp = daService.sprdFdlVerifyV3(da);
        return CommResponse.success(daZrResp);
    }

    @PostMapping("/mtk-download-record")
    public CommResponse mtkDownloadRecord(@RequestBody AuthRecordReq da, @RequestHeader("Scene") Integer scene) throws Exception {
        da.setScene(scene);
        if (StringUtils.isAnyEmpty(da.getVerifyData()) || ObjectUtils.isEmpty(da.getScene())) {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        return CommResponse.success(daService.mtkDownloadRecord(da));
    }

    @PostMapping("/sprd-fdl-record")
    public CommResponse sprdFdlRecord(@RequestBody AuthRecordReq da, @RequestHeader("Scene") Integer scene) throws Exception {
        da.setScene(scene);
        if (StringUtils.isAnyEmpty(da.getVerifyData()) || ObjectUtils.isEmpty(da.getScene())) {
            throw new CustomException(NetCodeEnum.PARAM_VERIFY_FAIL);
        }
        return CommResponse.success(daService.sprdFdlRecord(da));
    }
}
