package com.transsion.authentication.model.health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author： nan.hu on 2023/5/6
 * =======================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
@RestController
public class HealthController {

    @RequestMapping("/check")
    public void check(){

    }
}
