/*
 * SHENZHEN TRANSSION COMMUNICATION LIMITED TOP SECRET.
 * Copyright (c) 2016-2036  SHENZHEN TRANSSION COMMUNICATION LIMITED
 *
 *  PROPRIETARY RIGHTS of Shenzhen Transsion Communication Limited are involved in the
 *  subject matter of this material.  All manufacturing, reproduction, use,
 *  and sales rights pertaining to this subject matter are governed by the
 *  license agreement.  The recipient of this software implicitly accepts
 *  the terms of the license.
 *
 *   Description:
 *   Author: zengqiang.guo
 *   Version:
 *   Date:  19-10-11 下午4:00
 *   Modification:
 */

package com.transsion.daconsole.infrastructure.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus配置
 *
 * @author Mark sunlightcs@gmail.com
 * @since 2.0.0 2018-02-05
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PageInterceptor paginationInterceptor() {
        return new PageInterceptor();
    }
}
