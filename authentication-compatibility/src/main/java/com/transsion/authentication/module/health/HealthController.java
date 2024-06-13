package com.transsion.authentication.module.health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: K8S 心跳检测
 * @Author jiakang.chen
 * @Date 2023/2/8
 */
@RestController
public class HealthController {

    @RequestMapping("/check")
    public void check(){

    }
}
