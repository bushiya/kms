package com.transsion.authentication.model.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.transsion.authentication.model.bean.dto.DeviceAppQueryDTO;
import com.transsion.authentication.model.bean.req.DeviceAppQueryReq;
import com.transsion.authentication.model.bean.resp.DeviceAppResp;
import com.transsion.authentication.model.repository.entity.DeviceApp;
import com.transsion.authentication.model.repository.mapper.DeviceAppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 设备应用数据层
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Component
public class DeviceAppDao {

    @Resource
    private DeviceAppMapper deviceAppMapper;

    /**
     * 新增应用
     *
     * @param deviceApp 请求参数
     */
    public void addApp(DeviceApp deviceApp) {
        deviceApp.setCreateTime(new Date());
        deviceApp.setUpdateTime(new Date());
        deviceAppMapper.insert(deviceApp);
    }

    /**
     * 根据应用ID查询
     *
     * @param deviceApp 请求参数
     * @return 返回结果
     */
    public DeviceAppQueryDTO queryByAppId(DeviceApp deviceApp) {
        return deviceAppMapper.queryByAppId(deviceApp);
    }

    /**
     * 根据应用名称查询
     *
     * @param deviceApp 请求参数
     * @return 返回结果
     */
    public Integer queryByAppName(DeviceApp deviceApp) {
        return deviceAppMapper.queryByAppName(deviceApp);
    }

    /**
     * 根据应用包名查询
     *
     * @param deviceApp 请求参数
     * @return 返回结果
     */
    public Integer queryByAppPackage(DeviceApp deviceApp) {
        return deviceAppMapper.queryByAppPackage(deviceApp);
    }

    /**
     * 更新设备应用
     *
     * @param deviceApp 请求参数
     */
    public void updateDeviceApp(DeviceApp deviceApp) {
        deviceAppMapper.updateById(deviceApp);
    }

    /**
     * 应用接入设置查询
     *
     * @param pages    请求Page
     * @param queryReq 请求参数
     * @return 返回结果
     */
    public IPage<DeviceAppQueryDTO> search(Page<DeviceAppResp> pages, DeviceAppQueryReq queryReq) {
        return deviceAppMapper.search(pages, queryReq);
    }

}
