//package com.transsion.authenticationsdk.infrastructure.utils;
//
//import java.io.*;
//import java.util.Properties;
//
///*
// * 读取指定配置文件中指定配置信息的工具类
// */
//public class PropertiesUtils {
//
//    public static Properties properties;
//
//    public static String configFileName;
//
//    static {
//        properties = loadConfProperties("application.properties");
//        String environment = properties.getProperty("spring.profiles.active");
//        configFileName = "application" + "-" + environment + ".properties";
//        properties = loadConfProperties(configFileName);
//    }
//
//    /**
//     * 根据key获取内容
//     *
//     * @param key
//     * @return
//     */
//    public static String getValue(String key) {
//        String value = properties.getProperty(key);
//        return value;
//    }
//
//
//    /**
//     * 初始化 propertiies
//     *
//     * @return
//     */
//    public static Properties loadConfProperties(String fileName) {
//        Properties properties = new Properties();
//        InputStream in = null;
//        in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
//        try {
//            properties.load(in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return properties;
//    }
//}
