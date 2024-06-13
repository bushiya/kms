package com.transsion.authentication.module.auth.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.entity.ServerCommunicationEntity;
import com.transsion.authentication.module.auth.repository.mapper.ServerCommunicationMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/7/11
 */
@Component
public class ServerCommunicationDao {
    @Autowired
    ServerCommunicationMapper mapper;

    public void save(ServerCommunicationEntity data) {
        int insert = mapper.insert(data);
        if (insert != 1) {
            throw new CustomException(NetCodeEnum.APP_INIT_ERROR);
        }
    }

    public ServerCommunicationEntity selectByAppIdAndServerTag(String appId, String serverTag) {
        ServerCommunicationEntity serverCommunicationEntity = mapper.selectOne(new QueryWrapper<ServerCommunicationEntity>().eq("app_id", appId).eq("server_tag", serverTag));
        if (ObjectUtils.isEmpty(serverCommunicationEntity)) {
            throw new CustomException(NetCodeEnum.CUT_IN_ERROR);
        }
        return serverCommunicationEntity;
    }
}
