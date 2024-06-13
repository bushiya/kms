package com.transsion.testtool.module;

import com.alibaba.fastjson2.JSON;

import java.util.HashMap;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/9/11
 */
public class TestKey {
    public static void main(String[] args) {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("tips","123");
        objectObjectHashMap.put("url","https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png");
        System.out.println(JSON.toJSONString(objectObjectHashMap));
    }
}
