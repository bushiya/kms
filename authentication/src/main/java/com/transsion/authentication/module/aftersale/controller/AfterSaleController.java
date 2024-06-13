package com.transsion.authentication.module.aftersale.controller;

import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.module.aftersale.bean.req.AfterSaveAuthReq;
import com.transsion.authentication.module.aftersale.bean.resp.AfterSaveAuthResp;
import com.transsion.authentication.module.aftersale.repository.entity.AfterHeadEntity;
import com.transsion.authentication.module.aftersale.service.AfterSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 售后接口
 * @Author jiakang.chen
 * @Date 2023/6/21
 */
@RestController
@RequestMapping("after-sale")
public class AfterSaleController {

    @Autowired
    AfterSaleService afterSaleService;

    @PostMapping("auth-device")
    public CommResponse<AfterSaveAuthResp> authDevice(
            @RequestHeader(value ="Tool-Version", required = false) String toolVersion,
            @RequestHeader(value ="Chip-ID", required = false) String chipId,
            @RequestHeader(value = "IMEI", required = false) String imei,
            @RequestHeader(value ="Device-Model", required = false) String deviceModel,
            @RequestHeader(value = "Software-Version", required = false) String softwareVersion,
            @RequestHeader(value = "Point-ID", required = false) String pointId,
            @RequestBody @Validated AfterSaveAuthReq req) throws Exception {
        AfterHeadEntity heads = new AfterHeadEntity(toolVersion, chipId, imei, deviceModel, softwareVersion, pointId);
        return CommResponse.success(afterSaleService.deviceAuth(heads, req));
    }
}
