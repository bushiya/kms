package com.transsion.testtool.infrastructure.utils;

import java.security.SecureRandom;
import java.util.Random;


public class RandomUtil {

    /**
     * 生成16位不重复的随机数，含数字+大小写
     *
     * @return
     */
    public static String getRandomNumber() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * 生成 N 位不重复的随机数
     *
     * @return
     */
    public static String getRandomNumber(int size) {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < size; i++) {
            int type = rd.nextInt(10);
            uid.append(type);
        }
        return uid.toString();
    }

}