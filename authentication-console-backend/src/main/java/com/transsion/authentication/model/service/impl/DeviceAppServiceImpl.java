package com.transsion.authentication.model.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.transsion.authentication.infrastructure.enums.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.infrastructure.utils.ModelMapperUtils;
import com.transsion.authentication.infrastructure.utils.RSAUtil;
import com.transsion.authentication.infrastructure.utils.RedisUtil;
import com.transsion.authentication.model.bean.dto.DeviceAppQueryDTO;
import com.transsion.authentication.model.bean.req.DeviceAppQueryReq;
import com.transsion.authentication.model.bean.req.DeviceAppSaveReq;
import com.transsion.authentication.model.bean.req.DeviceAppStatusReq;
import com.transsion.authentication.model.bean.resp.DeviceAppResp;
import com.transsion.authentication.model.repository.dao.DeviceAppDao;
import com.transsion.authentication.model.repository.entity.DeviceApp;
import com.transsion.authentication.model.repository.mapper.DeviceAppMapper;
import com.transsion.authentication.model.service.DeviceAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Map;

/**
 * 设备应用实现类
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Service
public class DeviceAppServiceImpl extends ServiceImpl<DeviceAppMapper, DeviceApp> implements DeviceAppService {

    @Autowired
    private DeviceAppDao deviceAppDao;

    /**
     * 起停用 : 1.禁用  2.启用
     */
    @Value(value = "${kms.service.status}")
    private Integer status;


    /**
     * 新增应用
     *
     * @param saveReq 请求参数
     * @return
     */
    @Override
    public String addApp(DeviceAppSaveReq saveReq) {
        DeviceApp deviceApp = new DeviceApp().setStatus(status);
        ModelMapperUtils.map(saveReq, deviceApp);
        //2.验证数据
        verificationData(deviceApp);
        //3. 生成通信公私钥
        Map<String, String> map = RSAUtil.initRSAKey(2048);
        deviceApp.setServerPublicKey(map.get("publicKey"));
        deviceApp.setServerPrivateKey(map.get("privateKey"));
        //4. 数据落库
        deviceAppDao.addApp(deviceApp);
        //5. 缓存Redis
        RedisUtil.StringOps.set(saveReq.getAppId(), JSON.toJSONString(deviceApp));
        return map.get("publicKey");
    }

    /**
     * 验证数据 appPackage
     *
     * @param deviceApp 请求参数
     */
    private void verificationData(DeviceApp deviceApp) {
        DeviceAppQueryDTO appQueryDto = deviceAppDao.queryByAppId(deviceApp);
        if (!ObjectUtils.isEmpty(appQueryDto)) {
            throw new CustomException(NetCodeEnum.APP_ID_DATA_ALREADY_EXISTS.getCode(), NetCodeEnum.APP_ID_DATA_ALREADY_EXISTS.getMessageKey());
        }
        Integer appName = deviceAppDao.queryByAppName(deviceApp);
        if (!ObjectUtils.isEmpty(appName)) {
            throw new CustomException(NetCodeEnum.APP_NAME_DATA_ALREADY_EXISTS.getCode(), NetCodeEnum.APP_NAME_DATA_ALREADY_EXISTS.getMessageKey());
        }
        Integer packages = deviceAppDao.queryByAppPackage(deviceApp);
        if (!ObjectUtils.isEmpty(packages)) {
            throw new CustomException(NetCodeEnum.APP_PACKAGE_DATA_ALREADY_EXISTS.getCode(), NetCodeEnum.APP_PACKAGE_DATA_ALREADY_EXISTS.getMessageKey());
        }
    }

    /**
     * 变更状态
     *
     * @param statusReq 请求参数
     */
    @Override
    public void changeStatus(DeviceAppStatusReq statusReq) {
        DeviceApp deviceApp = new DeviceApp();
        ModelMapperUtils.map(statusReq, deviceApp);
        DeviceAppQueryDTO appQueryDto = deviceAppDao.queryByAppId(deviceApp);
        if (ObjectUtils.isEmpty(appQueryDto)) {
            throw new CustomException(NetCodeEnum.DATA_ALREADY_NOT_EXISTS.getCode(), NetCodeEnum.DATA_ALREADY_NOT_EXISTS.getMessageKey());
        }
        deviceApp.setId(appQueryDto.getId());
        deviceApp.setUpdateTime(new Date());
        deviceAppDao.updateDeviceApp(deviceApp);
        // 清除 Redis 缓存
        RedisUtil.KeyOps.delete(statusReq.getAppId());
    }

    /**
     * 查询应用接入设置
     *
     * @param queryReq 请求参数
     * @return 返回结果
     */
    @Override
    public Page<DeviceAppResp> search(DeviceAppQueryReq queryReq) {
        //1.构造Page对象
        Page<DeviceAppResp> pages = new Page<>(queryReq.getPageNo(), queryReq.getPageSize());
        //2.查询应用配置信息
        IPage<DeviceAppQueryDTO> appIPage = deviceAppDao.search(pages, queryReq);
        if (ObjectUtils.isEmpty(appIPage)) {
            return new Page<>();
        }
        //3.转换实体，并结果输出
        pages.setRecords(ModelMapperUtils.map(appIPage.getRecords(), DeviceAppResp.class));
        pages.setTotal(appIPage.getTotal());
        pages.setSize(appIPage.getSize());
        return pages;
    }


}
