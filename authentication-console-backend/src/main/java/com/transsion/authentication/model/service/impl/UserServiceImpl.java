package com.transsion.authentication.model.service.impl;

import com.transsion.authentication.model.service.UserService;
import com.transsion.authentication.model.repository.entity.User;
import com.transsion.authentication.model.repository.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户实现类
 *
 * @author donghai.yuan
 * @since 2023-05-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
