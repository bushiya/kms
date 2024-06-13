package com.transsion.daconsole.module.da.controller;

import com.github.pagehelper.PageInfo;
import com.transsion.daconsole.infrastructure.advice.CommResponse;
import com.transsion.daconsole.module.da.bean.req.DeviceListReq;
import com.transsion.daconsole.module.da.repository.entity.DaEntity;
import com.transsion.daconsole.module.da.service.DaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DaController {

    @Autowired
    private DaService daService;

    @PostMapping("/list")
    public CommResponse<PageInfo<DaEntity>> mtkDownloadVerify(@RequestBody DeviceListReq req) throws Exception {
        return CommResponse.success(daService.deviceList(req));
    }

}
