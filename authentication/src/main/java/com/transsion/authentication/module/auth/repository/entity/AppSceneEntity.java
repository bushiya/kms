package com.transsion.authentication.module.auth.repository.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description: App的场景列表
 * @Author jiakang.chen
 * @Date 2023/7/5
 */
@Data
@TableName("tb_app_scene")
public class AppSceneEntity {
    @TableId
    private Long id;
    private String appId;
    private String scene;
    private Integer algorithmCode;
    private Integer state;
    private Date createTime;
    private Date updateTime;
}
