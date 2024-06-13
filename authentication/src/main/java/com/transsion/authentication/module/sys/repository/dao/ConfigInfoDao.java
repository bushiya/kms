package com.transsion.authentication.module.sys.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.transsion.authentication.module.sys.repository.entity.ConfigInfo;
import com.transsion.authentication.module.sys.repository.mapper.ConfigInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 黑名单配置机型数据层
 *
 * @author donghai.yuan
 * @since 2023-05-22
 */
@Component
public class ConfigInfoDao {

    @Autowired
    private ConfigInfoMapper mapper;


    /**
     * 查询机型黑名单配置
     *
     * @return
     */
    public List<ConfigInfo> selectDeviceModelBlackList() {
        List<ConfigInfo> configInfos = mapper.selectList(new QueryWrapper<ConfigInfo>().eq("status", "2").eq("name", "device-model"));
        return configInfos;
    }
}
