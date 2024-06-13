package com.transsion.testtool.infrastructure.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.alibaba.fastjson2.JSON;
import com.transsion.testtool.infrastructure.constants.NetCodeEnum;
import com.transsion.testtool.infrastructure.exception.CustomException;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: RSA 工具类
 * @Author jiakang.chen
 * @Date 2023/2/8
 */
public class RSAUtil {
    // 密钥算法
    private final static String ALGORITHM_RSA = "RSA";

    //安装
    private final static String ALGORITHM_RSA1 = "RSA/ECB/PKCS1Padding";

    //签名算法
    public final static String SIGN_ALGORITHMS = "SHA256WithRSA";

    //编码格式
    private final static String CODE = "UTF-8";

    /**
     * 生成公钥、私钥的字符串
     * 方便传输
     *
     * @param modulus 密钥长度
     * @return 密钥键值对{publicKey:"",privateKey:""}
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> initRSAKey(int modulus) {
        HashMap<String, String> keyMap = null;
        try {
            keyMap = new HashMap<>(2);
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
            keyPairGen.initialize(modulus);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            String publicKey = Base64Encoder.encode(keyPair.getPublic().getEncoded());
            String privateKey = Base64Encoder.encode(keyPair.getPrivate().getEncoded());
            keyMap.put("publicKey", publicKey);
            keyMap.put("privateKey", privateKey);
            return keyMap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return keyMap;
        }
    }

    /**
     * 公钥字符串转公钥对象
     *
     * @param publicKeyStr 公钥字符串
     * @return 公钥对象
     */
    public static RSAPublicKey strByPublicKey(String publicKeyStr) {
        RSAPublicKey publicKey = null;
        try {
            // Java中RSAPublicKeySpec、X509EncodedKeySpec支持生成RSA公钥
            // 此处使用X509EncodedKeySpec生成
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            byte[] keyBytes = Base64Decoder.decode(publicKeyStr);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 私钥字符串转私钥对象
     *
     * @param privateKeyStr 私钥字符串
     * @return 私钥对象
     */
    public static RSAPrivateKey strByPrivateKey(String privateKeyStr) {
        RSAPrivateKey privateKey = null;
        try {
            // Java中只有RSAPrivateKeySpec、PKCS8EncodedKeySpec支持生成RSA私钥
            // 此处使用PKCS8EncodedKeySpec生成
            KeyFactory keyFactory1 = KeyFactory.getInstance(ALGORITHM_RSA);
            byte[] keyBytes1 = Base64Decoder.decode(privateKeyStr);
            PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(keyBytes1);
            privateKey = (RSAPrivateKey) keyFactory1.generatePrivate(spec1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }


    /**
     * 公钥加密
     *
     * @param data         需要加密的数据
     * @param publicKeyStr 公钥字符串
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String publicKeyStr) {
        String result = null;
        try {
            //将公钥字符串转为公钥对象
            RSAPublicKey publicKey = strByPublicKey(publicKeyStr);

            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA1);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长n转换成字节数
            int modulusSize = publicKey.getModulus().bitLength() / 8;
            // PKCS Padding长度为11字节，所以实际要加密的数据不能要 - 11byte
            int maxSingleSize = modulusSize - 11;
            // 切分字节数组，每段不大于maxSingleSize
            byte[][] dataArray = splitArray(data.getBytes(CODE), maxSingleSize);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 分组加密，并将加密后的内容写入输出字节流
            for (byte[] s : dataArray) {
                out.write(cipher.doFinal(s));
            }
            // 使用Base64将字节数组转换String类型
            result = Base64Encoder.encode(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 私钥解密
     *
     * @param data          公钥加密后的数据
     * @param privateKeyStr 私钥字符串
     * @return 解密后的数据
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String privateKeyStr) {
        //私钥字符串转私钥对象
        ByteArrayOutputStream out = null;
        try {
            RSAPrivateKey privateKey = strByPrivateKey(privateKeyStr);
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA1);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // RSA加密算法的模长 n
            int modulusSize = privateKey.getModulus().bitLength() / 8;
            byte[] dataBytes = data.getBytes(CODE);
            // 之前加密的时候做了转码，此处需要使用Base64进行解码
            byte[] decodeData = Base64Decoder.decode(dataBytes);
            // 切分字节数组，每段不大于modulusSize
            byte[][] splitArrays = splitArray(decodeData, modulusSize);
            out = new ByteArrayOutputStream();
            for (byte[] arr : splitArrays) {
                out.write(cipher.doFinal(arr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return new String(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 按指定长度切分数组
     *
     * @param data
     * @param len  单个字节数组长度
     * @return
     */
    private static byte[][] splitArray(byte[] data, int len) {

        int dataLen = data.length;
        if (dataLen <= len) {
            return new byte[][]{data};
        }
        byte[][] result = new byte[(dataLen - 1) / len + 1][];
        int resultLen = result.length;
        for (int i = 0; i < resultLen; i++) {
            if (i == resultLen - 1) {
                int slen = dataLen - len * i;
                byte[] single = new byte[slen];
                System.arraycopy(data, len * i, single, 0, slen);
                result[i] = single;
                break;
            }
            byte[] single = new byte[len];
            System.arraycopy(data, len * i, single, 0, len);
            result[i] = single;
        }
        return result;
    }

    /**
     * 签名
     *
     * @param data          待签名数据
     * @param privateKeyStr 私钥字符串
     * @return 签名后数据
     */
    public static String sign(String data, String privateKeyStr) {
        String result = null;
        try {
            //私钥字符串转私钥对象
            RSAPrivateKey privateKey = strByPrivateKey(privateKeyStr);
            byte[] keyBytes = privateKey.getEncoded();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey key = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(key);
            signature.update(data.getBytes(CODE));
            result = new String(Base64Encoder.encode(signature.sign()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }

    }


    /**
     * 验签
     *
     * @param srcData      原始数据
     * @param publicKeyStr 公钥字符串
     * @param sign         签名后的数据
     * @return 是否验签通过
     */
    public static void verify(String srcData, String publicKeyStr, String sign) {
        Boolean a = null;
        try {
            //公钥字符串转公钥对象
            RSAPublicKey publicKey = strByPublicKey(publicKeyStr);
            byte[] keyBytes = publicKey.getEncoded();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey key = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(key);
            signature.update(srcData.getBytes(CODE));
            a = signature.verify(Base64Decoder.decode(sign.getBytes()));
            if (!a) {
                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
            }
        } catch (Exception e) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
    }

    /**
     * 验签
     *
     * @param srcData   原始数据
     * @param publicKey 公钥对象
     * @param sign      签名后的数据
     * @return 是否验签通过
     */
    public static void verify(String srcData, PublicKey publicKey, String sign) {
        Boolean a = null;
        try {
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(publicKey);
            signature.update(srcData.getBytes(CODE));
            a = signature.verify(Base64Decoder.decode(sign.getBytes()));
            if (!a) {
                throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
            }
        } catch (Exception e) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
    }

    /**
     * 将JSON字符串转为 key=value&key=value的形式
     */
    public static String jsonToKeyValue(String str) {
        TreeMap jsonObject = JSON.parseObject(str, TreeMap.class);
        String result = "";
        for (Object o : jsonObject.keySet()) {
            result += o + "=" + jsonObject.get(o) + "&";
        }
        return result;
    }

    /**
     * 将key=value&key=value的形式转为 JSON字符串
     */
    public static String ketToJson(String str) {
        HashMap<String, String> map = new HashMap<>();
        String[] split = str.split("&");
        for (String s : split) {
            String[] data = s.split("=");
            map.put(data[0], data[1]);
        }
        return JSON.toJSONString(map);
    }

    /**
     * 将key=value&key=value的形式转为 JSON字符串
     */
    public static HashMap<String, String> ketToMap(String str) {
        HashMap<String, String> map = new HashMap<>();
        String[] split = str.split("&");
        for (String s : split) {
            String[] data = s.split("=");
            map.put(data[0], data[1]);
        }
        return map;
    }
}
