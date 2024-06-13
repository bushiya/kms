package com.transsion.authentication.model.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.model.bean.resp.ConfigInfoQueryResp;
import com.transsion.authentication.model.service.ConfigInfoService;
import com.transsion.authentication.model.bean.req.ConfigInfoQueryReq;
import com.transsion.authentication.model.bean.req.ConfigInfoSaveReq;
import com.transsion.authentication.model.bean.req.ConfigInfoStatusReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 黑名单机型配置信息控制器
 *
 * @author donghai.yuan
 * @since 2023-05-22
 */
@RestController
@RequestMapping("/abnormal-model")
public class ConfigInfoController {

    @Autowired
    private ConfigInfoService configInfoService;

    /**
     * 新增机型黑名单
     *
     * @param saveReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/insert-abnormal", method = RequestMethod.POST)
    public CommResponse insertAbnormal(@RequestBody @Valid ConfigInfoSaveReq saveReq) {
        configInfoService.insertAbnormal(saveReq);
        return CommResponse.success();
    }

    /**
     * 机型变更状态
     *
     * @param statusReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/change-status", method = RequestMethod.POST)
    public CommResponse changeStatus(@RequestBody @Valid ConfigInfoStatusReq statusReq) {
        configInfoService.changeStatus(statusReq);
        return CommResponse.success();
    }

    /**
     * 查询机型黑名单
     *
     * @param queryReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/query-model", method = RequestMethod.POST)
    public CommResponse<Page<ConfigInfoQueryResp>> queryModel(@RequestBody ConfigInfoQueryReq queryReq) {
        return CommResponse.success(configInfoService.queryModel(queryReq));
    }


}

