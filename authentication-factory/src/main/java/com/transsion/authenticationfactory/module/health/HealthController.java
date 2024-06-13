package com.transsion.authenticationfactory.module.health;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: K8S 心跳机制
 * @Author jiakang.chen
 * @Date 2023/6/19
 */
@RestController
public class HealthController {

    @RequestMapping("/check")
    public void check() {

    }

}
