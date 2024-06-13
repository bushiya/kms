package com.transsion.authentication.module.da.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.transsion.authentication.module.da.controller.req.AuthRecordReq;
import com.transsion.authentication.module.da.controller.req.DaReq;
import com.transsion.authentication.module.da.controller.rsp.DaResp;
import com.transsion.authentication.module.da.controller.rsp.DaZrResp;
import com.transsion.authentication.module.da.repository.entity.DaEntity;

public interface DaService extends IService<DaEntity> {
    /**
     * MTK 平台下载鉴权
     *
     * @param da
     * @return
     * @throws Exception
     */
//    DaResp mtkDownloadVerify(DaReq da) throws Exception;

    /**
     * 展锐 平台下载鉴权
     *
     * @param da
     * @return
     */
//    DaZrResp sprdFdlVerify(DaReq da);

    /**
     * MTK 平台下载鉴权
     *
     * @param da
     * @return
     * @throws Exception
     */
    DaResp mtkDownloadVerifyV2(DaReq da) throws Exception;

    /**
     * 展锐 平台下载鉴权
     *
     * @param da
     * @return
     */
    DaZrResp sprdFdlVerifyV2(DaReq da) throws Exception;

    /**
     * MTK 平台下载鉴权
     *
     * @param da
     * @return
     * @throws Exception
     */
    DaResp mtkDownloadVerifyV3(DaReq da) throws Exception;

    /**
     * 展锐 平台下载鉴权
     *
     * @param da
     * @return
     */
    DaZrResp sprdFdlVerifyV3(DaReq da) throws Exception;

    Boolean mtkDownloadRecord(AuthRecordReq da) throws Exception;

    Boolean sprdFdlRecord(AuthRecordReq da) throws Exception;
}
