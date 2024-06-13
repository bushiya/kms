package com.transsion.authentication.module.health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/6/8
 */
@RestController
public class HealthController {

    @RequestMapping("/check")
    public void check(){

    }
}
