package com.transsion.authentication.infrastructure.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: AES加密工具类
 * @Author jiakang.chen
 * @Date 2023/2/8
 */
public class AESUtil {

    //算法类型
    private static String ALGORITHM_AES = "AES";
    //填充格式
    private static String PADDING = "AES/CTR/NoPadding";
    //填充格式
    private static String ECB = "AES/ECB/PKCS5Padding";

    private static final String CONSTANT_PASSWORD = "kmsadmin88888888";

    public static void main(String[] args) throws Exception {
//        String encrypt = decrypt("AEXoIkxBSrjecyeD1jrY4w==", "F77H44mIxxv34qOb");
//        System.out.println(encrypt);
        String encrypt = encrypt("algo=1&kLoc=0&kType=1&sc=testAES", "1234567891012145");
        System.out.println(encrypt);
    }

    /**
     * 加密
     *
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_AES);
        //"算法/模式/补码方式" "AES/ECB/PKCS5Padding"
        Cipher cipher = Cipher.getInstance(ECB);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        //使用BASE64做转码功能，能起到2次加密的作用。
        return Base64Encoder.encode(encrypted);
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String key) throws Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_AES);
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance(ECB);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            //先base64解密
            byte[] encrypted1 = Base64Decoder.decode(content);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, StandardCharsets.UTF_8);
                return originalString;
            } catch (Exception e) {
                throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
            }
        } catch (Exception ex) {
            throw new CustomException(NetCodeEnum.DECRYPTION_FAILURE);
        }
    }


    /**
     * CTR 格式填充加密
     */
    public static String Encrypt(String plaintext, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(CONSTANT_PASSWORD.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64Encoder.encode(encryptedBytes);
    }

    /**
     * CTR 格式填充解密
     */
    public static String Decrypt(String ciphertext, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_AES);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(CONSTANT_PASSWORD.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(Base64Decoder.decode(ciphertext));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    private static byte[] hexStringToBytes(String hexString) {
        if (hexString.length() % 2 != 0) throw new IllegalArgumentException("hexString length not valid");
        int length = hexString.length() / 2;
        byte[] resultBytes = new byte[length];
        for (int index = 0; index < length; index++) {
            String result = hexString.substring(index * 2, index * 2 + 2);
            resultBytes[index] = Integer.valueOf(Integer.parseInt(result, 16)).byteValue();
        }
        return resultBytes;
    }

    private static String bytesToHexString(byte[] sources) {
        if (sources == null) return null;
        StringBuilder stringBuffer = new StringBuilder();
        for (byte source : sources) {
            String result = Integer.toHexString(source & 0xff);
            if (result.length() < 2) {
                result = "0" + result;
            }
            stringBuffer.append(result);
        }
        return stringBuffer.toString();
    }


    /**
     * 加密
     *
     * @param input 需要加密信息
     * @param key   密钥
     * @return 加密信息
     * @throws NoSuchAlgorithmException
     */
    public static String encryptByAES(String input, String key) throws Exception {
        // Cipher：密码，获取加密对象
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
        // 指定秘钥规则
        // 第一个参数表示：密钥，key的字节数组 长度必须是32位
        // 第二个参数表示：算法类型
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), ALGORITHM_AES);
        // 对加密进行初始化
        // 第一个参数：表示模式，有加密模式和解密模式
        // 第二个参数：表示秘钥规则
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // 进行加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        //将byte数组转为字符串
        return bytesToHexString(bytes);
    }

    /**
     * 解密
     *
     * @param input 加密后信息
     * @param key   密钥
     * @return 解密后的信息
     * @throws NoSuchAlgorithmException
     */
    public static String decryptByAES(String input, String key) throws Exception {
        // Cipher：密码，获取加密对象
        Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
        // 指定秘钥规则
        // 第一个参数表示：密钥，key的字节数组 长度必须是16位
        // 第二个参数表示：算法类型
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), ALGORITHM_AES);
        // 对加密进行初始化
        // 第一个参数：表示模式，有加密模式和解密模式
        // 第二个参数：表示秘钥规则
        cipher.init(Cipher.DECRYPT_MODE, sks);
        // 先将字符串转为byte数组进行解密
        byte[] inputBytes = hexStringToBytes(input);
        byte[] bytes = cipher.doFinal(inputBytes);
        return new String(bytes);
    }

}
