package com.transsion.authentication.model.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.dto.ConfigInfoQueryDTO;
import com.transsion.authentication.model.bean.req.ConfigInfoQueryReq;
import com.transsion.authentication.model.bean.resp.ConfigInfoQueryResp;
import com.transsion.authentication.model.repository.entity.ConfigInfo;
import com.transsion.authentication.model.repository.mapper.ConfigInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 黑名单配置机型数据层
 *
 * @author donghai.yuan
 * @since 2023-05-22
 */
@Component
public class ConfigInfoDao {

    @Autowired
    private ConfigInfoMapper configInfoMapper;

    /**
     * 新增机型黑名单
     *
     * @param configInfo 请求参数
     */
    public void insertAbnormal(ConfigInfo configInfo) {
        configInfo.setCreateTime(new Date());
        configInfo.setUpdateTime(new Date());
        configInfoMapper.insert(configInfo);
    }

    /**
     * 查询机型信息
     *
     * @param id 请求参数
     * @return 返回结果
     */
    public ConfigInfo queryById(Integer id) {
        return configInfoMapper.selectById(id);
    }

    /**
     * 更新机型信息
     *
     * @param configInfo 请求参数
     */
    public void updateConfigInfo(ConfigInfo configInfo) {
        configInfoMapper.updateById(configInfo);
    }

    /**
     * 查询机型信息
     *
     * @param pages    请求参数
     * @param queryReq 请求参数
     * @return 返回结果
     */
    public IPage<ConfigInfoQueryDTO> queryModel(Page<ConfigInfoQueryResp> pages, ConfigInfoQueryReq queryReq) {
        return configInfoMapper.queryModel(pages, queryReq);
    }

    /**
     * 按黑名单配置名称查询信息
     *
     * @param name 请求参数
     * @param value 请求参数
     * @return 返回结果
     */
    public ConfigInfoQueryDTO queryConfigInfoByNameAndValue(String name, String value) {
        return configInfoMapper.queryConfigInfoByNameAndValue(name, value);
    }

    /**
     * 按黑名单配置版本查询信息
     *
     * @param value 请求参数
     * @return 返回结果
     */
    public ConfigInfoQueryDTO queryConfigInByValue(String value) {
        return configInfoMapper.queryConfigInByValue(value);
    }
}
