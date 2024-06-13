package com.transsion.authentication.model.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.enums.NetCodeEnum;
import com.transsion.authentication.infrastructure.enums.StatusEnum;
import com.transsion.authentication.infrastructure.utils.ModelMapperUtils;
import com.transsion.authentication.model.bean.dto.ConfigInfoQueryDTO;
import com.transsion.authentication.model.bean.req.ConfigInfoQueryReq;
import com.transsion.authentication.model.bean.req.ConfigInfoSaveReq;
import com.transsion.authentication.model.bean.req.ConfigInfoStatusReq;
import com.transsion.authentication.model.bean.resp.ConfigInfoQueryResp;
import com.transsion.authentication.model.repository.dao.ConfigInfoDao;
import com.transsion.authentication.model.repository.entity.ConfigInfo;
import com.transsion.authentication.model.repository.mapper.ConfigInfoMapper;
import com.transsion.authentication.model.service.ConfigInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;


/**
 * 黑名单机型配置信息实现类
 *
 * @author donghai.yuan
 * @since 2023-05-22
 */
@Service
public class ConfigInfoServiceImpl extends ServiceImpl<ConfigInfoMapper, ConfigInfo> implements ConfigInfoService {

    @Autowired
    private ConfigInfoDao configInfoDao;

    /**
     * 新增黑名单机型信息
     *
     * @param saveReq 请求参数
     */
    @Override
    public void insertAbnormal(ConfigInfoSaveReq saveReq) {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setName(saveReq.getName());
        configInfo.setValue(saveReq.getValue());
        configInfo.setDes(saveReq.getDes());
        configInfo.setStatus(StatusEnum.ENABLE.getCode());
        //2.验证数据
        verificationData(configInfo);
        configInfoDao.insertAbnormal(configInfo);
    }

    /**
     * 校验数据
     *
     * @param configInfo 请求参数
     */
    private void verificationData(ConfigInfo configInfo) {
        ConfigInfoQueryDTO byinfo = configInfoDao.queryConfigInfoByNameAndValue(configInfo.getName(),configInfo.getValue());
        if (!ObjectUtils.isEmpty(byinfo)) {
            throw new CustomException(NetCodeEnum.BLACKLIST_MODEL_NAME_AND_MODEL_VALUE_SAME.getCode(), NetCodeEnum.BLACKLIST_MODEL_NAME_AND_MODEL_VALUE_SAME.getMessageKey());
        }
        ConfigInfoQueryDTO certValue = configInfoDao.queryConfigInByValue(configInfo.getValue());
        if (!ObjectUtils.isEmpty(certValue)) {
            throw new CustomException(NetCodeEnum.BLACKLIST_MODEL_VALUE_DATA_ALREADY_EXISTS.getCode(), NetCodeEnum.BLACKLIST_MODEL_VALUE_DATA_ALREADY_EXISTS.getMessageKey());
        }
    }

    /**
     * 机型变更状态
     *
     * @param statusReq 请求参数
     */
    @Override
    public void changeStatus(ConfigInfoStatusReq statusReq) {
        ConfigInfo configInfo = configInfoDao.queryById(statusReq.getId());
        if (ObjectUtils.isEmpty(configInfo)) {
            throw new CustomException(NetCodeEnum.DATA_ALREADY_NOT_EXISTS.getCode(), NetCodeEnum.DATA_ALREADY_NOT_EXISTS.getMessageKey());
        }
        configInfo.setUpdateTime(new Date());
        configInfo.setStatus(statusReq.getStatus());
        configInfoDao.updateConfigInfo(configInfo);
    }

    /**
     * 查询机型信息
     *
     * @param queryReq 请求参数
     * @return 返回结果
     */
    @Override
    public Page<ConfigInfoQueryResp> queryModel(ConfigInfoQueryReq queryReq) {
        Page<ConfigInfoQueryResp> pages = new Page<>(queryReq.getPageNo(), queryReq.getPageSize());
        //2.查询应用配置信息
        IPage<ConfigInfoQueryDTO> appIPage = configInfoDao.queryModel(pages, queryReq);
        if (ObjectUtils.isEmpty(appIPage)) {
            return new Page<>();
        }
        pages.setRecords(ModelMapperUtils.map(appIPage.getRecords(), ConfigInfoQueryResp.class));
        pages.setTotal(appIPage.getTotal());
        pages.setSize(appIPage.getSize());
        return pages;
    }
}
