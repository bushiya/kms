package com.transsion.authentication.model.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.infrastructure.advice.CommResponse;
import com.transsion.authentication.model.bean.req.BlacklistCertQueryReq;
import com.transsion.authentication.model.bean.req.BlacklistCertSaveReq;
import com.transsion.authentication.model.bean.req.BlacklistCertStatusReq;
import com.transsion.authentication.model.bean.resp.BlacklistCertQueryResp;
import com.transsion.authentication.model.service.BlacklistCertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 黑名单证书控制器
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Slf4j
@RestController
@RequestMapping("/abnormal-cert")
public class BlacklistCertController {

    @Autowired
    private BlacklistCertService blacklistCertService;

    /**
     * 新增证书黑名单
     *
     * @param saveReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/insert-abnormal", method = RequestMethod.POST)
    public CommResponse insertAbnormal(@RequestBody @Valid BlacklistCertSaveReq saveReq) {
        blacklistCertService.insertAbnormal(saveReq);
        return CommResponse.success();
    }

    /**
     * 证书变更状态
     *
     * @param statusReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/change-status", method = RequestMethod.POST)
    public CommResponse changeStatus(@RequestBody @Valid BlacklistCertStatusReq statusReq) {
        blacklistCertService.changeStatus(statusReq);
        return CommResponse.success();
    }

    /**
     * 查询证书黑名单
     *
     * @param queryReq 请求参数
     * @return 返回结果
     */
    @RequestMapping(value = "/query-cert", method = RequestMethod.POST)
    public CommResponse<Page<BlacklistCertQueryResp>> queryCert(@RequestBody BlacklistCertQueryReq queryReq) {
        return CommResponse.success(blacklistCertService.queryCert(queryReq));
    }


}

