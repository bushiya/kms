package com.transsion.authentication.model.repository.mapper;

import com.transsion.authentication.model.repository.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户 Mapper 接口
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
