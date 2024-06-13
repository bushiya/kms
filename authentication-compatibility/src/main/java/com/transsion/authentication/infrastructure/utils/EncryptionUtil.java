package com.transsion.authentication.infrastructure.utils;

import com.asa.dwbcryptowrapperserver.DwbcryptoWrapperServer;
import com.transsion.authentication.infrastructure.constants.NetCodeEnum;
import com.transsion.authentication.infrastructure.exception.CustomException;
import com.transsion.authentication.module.auth.repository.resource.EncryptionSource;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @Description: 白盒sdk方法封装
 * @Author wei.chen8
 * @Date 2023/2/27
 */
@Data
@Component
public class EncryptionUtil implements ApplicationContextAware {

    @Autowired
    EncryptionSource encryptionSource;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        serverKeyIdStr = DwbcryptoWrapperServer.asaDwbcryptoGetLatestKeyIdServer(encryptionSource.getFilePath() + "asaServer.key");
        asaWbcryptoServerRoundKeys = DwbcryptoWrapperServer.asaDwbcryptoGetServerKeyServer(serverKeyIdStr, encryptionSource.getFilePath() + "asaServer.key");
        clientServerKeyIdStr = DwbcryptoWrapperServer.asaDwbcryptoGetLatestKeyIdServer(encryptionSource.getFilePath() + "asaClientServer.key");
        asaWbcryptoClientServerRoundKeys = DwbcryptoWrapperServer.asaDwbcryptoGetServerKeyServer(clientServerKeyIdStr, encryptionSource.getFilePath() + "asaClientServer.key");
    }

    private String serverKeyIdStr;

    private int[] asaWbcryptoServerRoundKeys;

    private String clientServerKeyIdStr;

    private int[] asaWbcryptoClientServerRoundKeys;

    public static void main1(String[] args) {
//        4E0244AB4CF7F14EE58378E017E903ED,3D2CBB77572E5A14F98B575BB759498B,42E20728A64B2A778D8786F4A1A485AA
        String inputSha256Str = "[\"40E4400C5C90F79D8F390584EEBAD893AC9BDBA0FF1507B126D4C9DB547929DA\"]";
        String inputMd5Str = "[\"02025FE71F8C942D4F35C494543DE558\",\"3D2CBB77572E5A14F98B575BB759498B\",\"42E20728A64B2A778D8786F4A1A485AA\"]";

        String authKeySha256Str = DwbcryptoWrapperServer.asaDwbcryptoGenAuthKeyServer(inputSha256Str, 1);
        String authKeyMd5Str = DwbcryptoWrapperServer.asaDwbcryptoGenAuthKeyServer(inputMd5Str, 0);
        System.out.println(authKeySha256Str);
        System.out.println(authKeyMd5Str);
    }

    public static void main(String[] args) {
        initKey();
    }

    public static void initKey() {
        String filePath = "C:\\Users\\jiakang.chen\\Desktop\\3\\";
        /**
         * 服务端生成的密钥对
         */
        String asaServerKeyFilePath = filePath + "asaServer.key";
        String asaClientServerKeyFilePath = filePath + "asaClientServer.key";
        /**
         * 客户端生成的密钥对
         */
        String asaClientKeyFilePath = filePath + "asaClient.key";
        String asaServerClientKeyFilePath = filePath + "asaServerClient.key";

        DwbcryptoWrapperServer.asaDwbcryptoGenKeyPairsServer(asaServerKeyFilePath, asaClientServerKeyFilePath, asaClientKeyFilePath, asaServerClientKeyFilePath);

    }

    /**
     * 加密数据体
     *
     * @param data 数据体
     * @return 加密结果
     */
    public String encrypt(String data) {
        if (StringUtils.isBlank(data)) {
            throw new CustomException("data为空");
        }
        return DwbcryptoWrapperServer.asaDwbcryptoEncryptStringWrapperServer(data, asaWbcryptoServerRoundKeys);
    }

    /**
     * 解密数据体
     *
     * @param data 加密后的数据
     * @return 解密后的数据
     */
    public String decode(String data) {
        if (StringUtils.isBlank(data)) {
            throw new CustomException("data为空");
        }
        return DwbcryptoWrapperServer.asaDwbcryptoDecryptStringWrapperClientServer(data, asaWbcryptoClientServerRoundKeys);
    }


    /**
     * 签名
     *
     * @param data 需要签名的信息
     * @return 签名信息
     */
    public String sign(String data) {
        if (StringUtils.isBlank(data)) {
            throw new CustomException("data为空");
        }
        return DwbcryptoWrapperServer.asaDwbcryptoSignStringWrapperClientServer(data, asaWbcryptoClientServerRoundKeys);
    }

    /**
     * 验签
     *
     * @param data 加签名的数据体
     * @param sign 签名信息
     * @return 是否验签成功
     */
    public boolean verify(String data, String sign) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(sign)) {
            throw new CustomException("data或sign为空");
        }
        Boolean aBoolean = DwbcryptoWrapperServer.asaDwbcryptoVerifyStringWrapperServer(data, sign, asaWbcryptoServerRoundKeys) ? Boolean.TRUE : Boolean.FALSE;
        if (!aBoolean) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
        return aBoolean;
    }

    /**
     * 验签
     *
     * @param data 加签名的数据体
     * @param sign 签名信息
     * @return 是否验签成功
     */
    public boolean verify(String data, byte[] sign) {
        boolean valid = DwbcryptoWrapperServer.asaDwbcryptoVerifyBytesWrapperServer(data.getBytes(StandardCharsets.UTF_8), sign, asaWbcryptoServerRoundKeys);
        if (!valid) {
            throw new CustomException(NetCodeEnum.SIGN_VERIFICATION_FAILED);
        }
        return valid;
    }


    public static byte[] hexToBytes(String hex) {
        int hexLength = hex.length();
        byte[] result;
        //判断Hex字符串长度，如果为奇数个需要在前边补0以保证长度为偶数
        //因为Hex字符串一般为两个字符，所以我们在截取时也是截取两个为一组来转换为Byte。
        if (hexLength % 2 == 1) {
            //奇数
            hexLength++;
            hex = "0" + hex;
        }
        result = new byte[(hexLength / 2)];
        int j = 0;
        for (int i = 0; i < hexLength; i += 2) {
            result[j] = hexToByte(hex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static byte hexToByte(String hex) {
        return (byte) Integer.parseInt(hex, 16);
    }

}
