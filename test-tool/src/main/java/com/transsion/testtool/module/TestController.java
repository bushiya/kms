package com.transsion.testtool.module;

import cn.hutool.core.codec.Base64Decoder;
import com.alibaba.fastjson2.JSON;
import com.transsion.authenticationsdk.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationsdk.infrastructure.core.KmsClient;
import com.transsion.authenticationsdk.infrastructure.core.KmsClientBuilder;
import com.transsion.authenticationsdk.infrastructure.exception.SdkCustomException;
import com.transsion.testtool.infrastructure.utils.AESUtil;
import com.transsion.testtool.infrastructure.utils.CsrUtil;
import com.transsion.testtool.infrastructure.utils.RSAUtil;
import com.transsion.testtool.infrastructure.utils.RsaByteUtil;
import com.transsion.testtool.module.bean.req.EncodeReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/8/7
 */
@Slf4j
@RestController
public class TestController {

    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkf4lNFJNfcl4d4IvHP2/SJNGWxmSlOmgou/xDZ5aXnvcZVKeG4vVwgGJearU/wvAk9ScX7/M5Pn6ueenEpMC7LV32vy6SFnA1GjA+VyPaJn3BHA36G71jAz0nxbSpah0fK1/HZ1DYTcmgb1JxuXGj8eT0EqawftrJC7apDl0fsjrILSCYzF/ZYJtwBdYZQvU5dth6Y7/jA+lbneZ+ZndYZEEThVc+fIPcK5jF9ljB3pAfrO3pcI/UDIT3Vbb510IRNnc8scoKb3ff7uOpoaPFQKMuC/yfJ196HF5ABB52+oSL0qvTx/MRklN3V21osQBs8ufBunyN8vTjB9bbVbfRwIDAQAB";
    private String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCR/iU0Uk19yXh3gi8c/b9Ik0ZbGZKU6aCi7/ENnlpee9xlUp4bi9XCAYl5qtT/C8CT1Jxfv8zk+fq556cSkwLstXfa/LpIWcDUaMD5XI9omfcEcDfobvWMDPSfFtKlqHR8rX8dnUNhNyaBvUnG5caPx5PQSprB+2skLtqkOXR+yOsgtIJjMX9lgm3AF1hlC9Tl22Hpjv+MD6Vud5n5md1hkQROFVz58g9wrmMX2WMHekB+s7elwj9QMhPdVtvnXQhE2dzyxygpvd9/u46mho8VAoy4L/J8nX3ocXkAEHnb6hIvSq9PH8xGSU3dXbWixAGzy58G6fI3y9OMH1ttVt9HAgMBAAECggEAHN7FPLOJ33YX/IoiBvmm+hMltp43/nq06YPJh4fpFwYnb2IHIitk+hiTtxuxQz5vtsw0oDqtqhpFGr5Uq7emvGimawfPTr9xApA0mRtUwLqxvo7yf2Rx6mAlf9Bjl57Yak9k89ZFnNPrKXzlOg/2VDiDYo1eTT4K0fzx/8fc4bVNUKvM7n4g8+mlx2zIelOjsooCbQC2dpdhI1eoGY7S8AQw2bYk6au19tysUl/xD+lwvP3FZKZkTGkhbSJcX/sIyxKdummlCkFqVizvxD4j7elc0Ty+CW+GeqYLL8U9D6BP+XFw2Rotrb4/WBxDupjgCySaQkKHbjMp7mPKhBSoAQKBgQDE6vp68fFwVG823lezsaGanpO0amyqmPu20txz5fxbxrQ7TJYqLwDaGoM5LqUb0+QR9i/qxTvSpCchafvu5c9kdccrZaFAbFx2wzqFhWDMRedvmUIASDS9Iw8BDaza/oEoH1APEX4UR54HW7V/oWxVujIKaDWp2kFCtOcX3iunYQKBgQC9y6uJxHgCs1CnDuSfgOx6oWY8h0oV6VKsV3tRWfUfBTrzZdzGtR9b2ANHUoXAQFZTszYBSx3zkdyH0XaM4XxmX+oQvayNlrHpN++JGy8UvmimZJZqLnPRAS/JDoJXBoxR2Zcu4wYWpzQurVvPJ1Xhn8AIKJCxmG7oq/dMQM4PpwKBgHlnebhU9ArSM6L7X8zMphqiy+O+fXKMURw9UmcuEvEuR64LoMUvJ3JQSDj39hl7ALDGPHKc2S4bwHhMVGPMUZnZN0sdJFaIuFUMNkddnPkz2+Uk8tkjq0g0liqLGNgHcu+epogzu6ybHuXgd7zRc+5CllluOC+Y/QU4bQV9FyLhAoGAawcYc7ccKgGugJaQbDYKpNixS37LU3Hg0hj/DJAa/2Q2qx0NE+z9gxBcQPMTQ6fERqzvzmjqufjOELUZEjbxrNV3q/sZ/Do3l5mYdYuj0CHT58kfXT9WxkzSAqIw9BxYMqvual1RiKK/dXcDJ905u+MXKkKd3dhk6sZWzWRaD08CgYBXgqaG9chwp/sAikZeJSPIkcHu6kq6IITg6EHN722Ldb0HzafRzl98Y3oZYILM57key9/GnwZgla3qGJxGqOn+XhVDFM9HlKQZwCDpyOF0uKJqc+/upAnVYsIbalar739OF7tl7zob4o11+P3d/n6gxfrV8z0tvLHpfma3pPIuzw==";

    private static String sdkPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm5qpx0oUICNE263ABE1mApnN5njqLEZwUk9IfqVkcrAgsV6a4DkDHRAs0mXvQIQzuim7JuwPzuLOalk+WZXhYv67ciDVtxTdC0NGixUr3goLUWKL1nwD2865+2JAmRnphJV9d6JJSsaF3QPiken58X21WqbrzDFWriFNLNhlokqqxouKktpLU73FhmJjsJ6cN+HZ2E8tackpY9Uva6p4cRRoaXKVPpEGd+PPd+gVcuQLVw6JBWRHDNAOTqR5qmnMIf0eA/01dvS+iUIZ+bNbO6MVka0LjAGST47uqngSPPXQg5vk/ypoLz8RVieKrhF45MG8cEEwV7eHrOovV+s8HQIDAQAB";
    private static String sdkPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbmqnHShQgI0TbrcAETWYCmc3meOosRnBST0h+pWRysCCxXprgOQMdECzSZe9AhDO6Kbsm7A/O4s5qWT5ZleFi/rtyINW3FN0LQ0aLFSveCgtRYovWfAPbzrn7YkCZGemElX13oklKxoXdA+KR6fnxfbVapuvMMVauIU0s2GWiSqrGi4qS2ktTvcWGYmOwnpw34dnYTy1pySlj1S9rqnhxFGhpcpU+kQZ348936BVy5AtXDokFZEcM0A5OpHmqacwh/R4D/TV29L6JQhn5s1s7oxWRrQuMAZJPju6qeBI89dCDm+T/KmgvPxFWJ4quEXjkwbxwQTBXt4es6i9X6zwdAgMBAAECggEAG7evKnq9nwPpTiBjJeELnMb88GL1g7GAbIkG5thbTjqleDLb77j4HwM0mL3jomBqPm0Uk1hVcIriLft7qcWTAmlRxoCm0Rf8T/dJ55h72e98GKEvO6OXpF3sryTPUPZWrHFciiT0XUeDrNz2TPuatDOqTF2TfyoCwL3Z23UolYHimegQqIhhjB2pJzTj2ANEcop9utxSl2uIlf6J9F2FHLc+rHwLzqtThoyera8rg37BHqtDELx65+75rgJjlrZDtwj+x+xMnf+4/MN4aw5r9OxM7tZFbSqD0EoOHMpaAQH6+WhDZxHP9JXw9pJsY5+hFEslZwK0fk7qVfR+J2LYGQKBgQD4ngzDBj1EuU06IEHdNs4SugfXkAYed0ESe31/N1Ux53VdYrtSmfxss8zW9+zRDfSN2IlymfpyRfFfdUjF4KP/l4uvmBKYptveQGeF+bZBJRrxy8fwKcBWWc3rYElc+pd73qPPUGU2rmAOF+D4mA8AZevahOf9E0L3xH2BqA7K4wKBgQCgOYqb2nuPbgdYSQEZjmdPsNaJxj6tD8hA+lfz25Nj356HuIt7xj0ZcJSkJ29rzuQS2Aamfl6+Lko7ThtqQf21cWJvleirwxBYk6pwmEojcbUNuzYE/BEOdiEvWUJtQhsNed+M3CMG/wJqhJf+gJWkqYxiBonDGZM0cdBdmsmM/wKBgFqLVSKB1zZ8NgABV3t1Rj/TuJ9tOI1H6U8N4nq+cCyomznTh11h72i3Yta3nGvWpUYzons2UNvlVS7qzAIk+hVP3/Oncr5dpp2e3xUQCKxZEyMUkFCLJB1Ov4wFmTJfoWPfhdEu+m2bvCfl0m03E9+VPvxqgwLMTQtnaD4cMqb5AoGAWCPPUDU8S4anm7VtWXWw93G8U0YvLFzGOJh+0bM0vXhCaT7n3EsNSXVZD4RyEshZRtR3tBdIXy5vv3gUNLvgqXOaRvAccoB3+YIh/JaKEj127/rQmKVhbKcnUji0Bg6beypUA9UFn8+gsKAITQTHTTHH/BTErALhq7QAf72qqlkCgYEAzuZwvPYBA0Yo3vx+CFXY/bVb8Acr7QO0QH6BLMNYN6qGGuiDrtb8Yrz2hsfbJl0cc5qo4RWWCqiVfH5eQsVSYl8duv9A4KDc0A0mH9clGpPWlQxyjvTOcowPllarAhRIlc60F80G22GAcv+5czYT8nFeLEtZBp6hK+rzIRjUjRA=";
    private static String serverPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkQyod9JFj5X25TuZCvXrZagpc//f70deucmsVw5NGrZ8SD7dMR2K0rZQ2dgexKdsI86k+DUhXS26CYYnd8rRZbvsOdvRkrhIASHHKmDbs0lH0W6AVFNqnDOGV22yu4JDch+rqQ7900wYZJAqVjRjFdHXMNpwtF2MD1+vwf5Z1cJ+EIwK/Gcr1GMi7n1OSwbmn9C8Cclm3sZ8xBp9g026H2WD24dL9TALnY3Lpwbkvx+4y0idwCUncfhYolOxtKmewe09ytMREhU3dwhf3dY+lnwE8gtjykz5vD6D73delQRmQk7jNKxYNOL+u0A4lxsDOSIPGwpYImdhoE0dCVCs2wIDAQAB";

    String hemCert = "MIID0jCCArqgAwIBAgIBATANBgkqhkiG9w0BAQsFADBmMSEwHwYJKoZIhvcNAQkBDBJUZXN0QHRyYW5zc2lvbi5jb20xCzAJBgNVBAYTAkhLMQ0wCwYDVQQKEwRUZXN0MQswCQYDVQQLEwIwMDELMAkGA1UECxMCMTUxCzAJBgNVBAMTAk5OMB4XDTIzMDcxMTEyNDcyMloXDTI2MDcxMTEyNDcyMlowZjEhMB8GCSqGSIb3DQEJAQwSVGVzdEB0cmFuc3Npb24uY29tMQswCQYDVQQGEwJISzENMAsGA1UEChMEVGVzdDELMAkGA1UECxMCMDAxCzAJBgNVBAsTAjE1MQswCQYDVQQDEwJOTjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAI3dnIB1NOMJwf2sH8/O627mFVazXHbZcTx89fDZE3uW+0p2JoP7DYNU8zE49pXFUXZ57BQeceNBdYp7t+wHPAwkoRh8x6SWh/A314ZL45E1y++5Uh5FQMyYrJ2rLxCyGrNldTCwYIIyYkDwBkVEqhX6FSV71CdiZYGh0+yXaZNyAMCk+vzn6/gQO/ln2xGznlT3kvX2lQDX5nSxObdox5fTWo7v6XAsUJ5r2d/9DYelKA7ZvtmjxLSrFt0nG8cJ+vGQ9vOqCNobvMD0ZIOaRAiOmunwatm4S7gy8U0bVGutWxJ7wTKXwnMFUfbH8P3zWbeVYA24o2k/YvRf2rAIn4cCAwEAAaOBijCBhzBqBgNVHSAEYzBhMF8GDisGAQQBlRICAgEBAQEBME0wSwYIKwYBBQUHAgIwPww9VGhpcyBjZXJ0aWZpY2F0ZSBtYXkgYmUgdXNlZCBmb3IgZGVtb25zdHJhdGlvbiBwdXJwb3NlcyBvbmx5LjAMBgNVHRMEBTADAQH/MAsGA1UdDwQEAwIBtjANBgkqhkiG9w0BAQsFAAOCAQEAT//rHec7WmQXeAW4AEzwxCD2jWhoZIfqeubw49+yGOCnzNX8Mdy/CtfDMAvkDwP50VVyOkc+21W0VJFJVrQaTKOetx+zn6e2J2ch1MdRwrL00s3DiWKyrnBiysr0CjGWlPee5iHYsMPzFhXeJH5WdOOzxW30/uwLVY9p6tjJMdDOXepqjGGfI2uzd4luDuxx1MNi0rHAiY56hDiyZ8jo6VU9bH6q3QV9ZIAJywSFrNYUYm1bw2aiRELlORIh8h01iWBHFlJs3hku559NLO9E96rPkbmUmmw/3P2r0MaaKzLPDaRlAwWHvqdm+KXT5IO+jDtYaMl8PHloVLWmKueVoA==";


    private KmsClient kmsClient;

    {
        kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/auth").appId("appId")
                .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
    }


    @RequestMapping("getCsr")
    public String getCsr() throws Exception {
        RSAPublicKey rsaPublicKey = RSAUtil.strByPublicKey(publicKey);
        RSAPrivateKey rsaPrivateKey = RSAUtil.strByPrivateKey(privateKey);
        System.out.println(rsaPrivateKey);
        System.out.println(rsaPublicKey);
        String csr = CsrUtil.generateCsrString(rsaPublicKey, rsaPrivateKey);
        System.out.println(csr);
        return csr;
    }

    public static void main1(String[] args) {
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkf4lNFJNfcl4d4IvHP2/SJNGWxmSlOmgou/xDZ5aXnvcZVKeG4vVwgGJearU/wvAk9ScX7/M5Pn6ueenEpMC7LV32vy6SFnA1GjA+VyPaJn3BHA36G71jAz0nxbSpah0fK1/HZ1DYTcmgb1JxuXGj8eT0EqawftrJC7apDl0fsjrILSCYzF/ZYJtwBdYZQvU5dth6Y7/jA+lbneZ+ZndYZEEThVc+fIPcK5jF9ljB3pAfrO3pcI/UDIT3Vbb510IRNnc8scoKb3ff7uOpoaPFQKMuC/yfJ196HF5ABB52+oSL0qvTx/MRklN3V21osQBs8ufBunyN8vTjB9bbVbfRwIDAQAB";
        String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCR/iU0Uk19yXh3gi8c/b9Ik0ZbGZKU6aCi7/ENnlpee9xlUp4bi9XCAYl5qtT/C8CT1Jxfv8zk+fq556cSkwLstXfa/LpIWcDUaMD5XI9omfcEcDfobvWMDPSfFtKlqHR8rX8dnUNhNyaBvUnG5caPx5PQSprB+2skLtqkOXR+yOsgtIJjMX9lgm3AF1hlC9Tl22Hpjv+MD6Vud5n5md1hkQROFVz58g9wrmMX2WMHekB+s7elwj9QMhPdVtvnXQhE2dzyxygpvd9/u46mho8VAoy4L/J8nX3ocXkAEHnb6hIvSq9PH8xGSU3dXbWixAGzy58G6fI3y9OMH1ttVt9HAgMBAAECggEAHN7FPLOJ33YX/IoiBvmm+hMltp43/nq06YPJh4fpFwYnb2IHIitk+hiTtxuxQz5vtsw0oDqtqhpFGr5Uq7emvGimawfPTr9xApA0mRtUwLqxvo7yf2Rx6mAlf9Bjl57Yak9k89ZFnNPrKXzlOg/2VDiDYo1eTT4K0fzx/8fc4bVNUKvM7n4g8+mlx2zIelOjsooCbQC2dpdhI1eoGY7S8AQw2bYk6au19tysUl/xD+lwvP3FZKZkTGkhbSJcX/sIyxKdummlCkFqVizvxD4j7elc0Ty+CW+GeqYLL8U9D6BP+XFw2Rotrb4/WBxDupjgCySaQkKHbjMp7mPKhBSoAQKBgQDE6vp68fFwVG823lezsaGanpO0amyqmPu20txz5fxbxrQ7TJYqLwDaGoM5LqUb0+QR9i/qxTvSpCchafvu5c9kdccrZaFAbFx2wzqFhWDMRedvmUIASDS9Iw8BDaza/oEoH1APEX4UR54HW7V/oWxVujIKaDWp2kFCtOcX3iunYQKBgQC9y6uJxHgCs1CnDuSfgOx6oWY8h0oV6VKsV3tRWfUfBTrzZdzGtR9b2ANHUoXAQFZTszYBSx3zkdyH0XaM4XxmX+oQvayNlrHpN++JGy8UvmimZJZqLnPRAS/JDoJXBoxR2Zcu4wYWpzQurVvPJ1Xhn8AIKJCxmG7oq/dMQM4PpwKBgHlnebhU9ArSM6L7X8zMphqiy+O+fXKMURw9UmcuEvEuR64LoMUvJ3JQSDj39hl7ALDGPHKc2S4bwHhMVGPMUZnZN0sdJFaIuFUMNkddnPkz2+Uk8tkjq0g0liqLGNgHcu+epogzu6ybHuXgd7zRc+5CllluOC+Y/QU4bQV9FyLhAoGAawcYc7ccKgGugJaQbDYKpNixS37LU3Hg0hj/DJAa/2Q2qx0NE+z9gxBcQPMTQ6fERqzvzmjqufjOELUZEjbxrNV3q/sZ/Do3l5mYdYuj0CHT58kfXT9WxkzSAqIw9BxYMqvual1RiKK/dXcDJ905u+MXKkKd3dhk6sZWzWRaD08CgYBXgqaG9chwp/sAikZeJSPIkcHu6kq6IITg6EHN722Ldb0HzafRzl98Y3oZYILM57key9/GnwZgla3qGJxGqOn+XhVDFM9HlKQZwCDpyOF0uKJqc+/upAnVYsIbalar739OF7tl7zob4o11+P3d/n6gxfrV8z0tvLHpfma3pPIuzw==";
        RSAUtil.verify("123", publicKey, RSAUtil.sign("123", privateKey));
        System.out.println(123);
    }

    @RequestMapping("sign")
    public String sign(String data) {
        return RSAUtil.sign(data, privateKey);
    }


    @RequestMapping("encodes")
    public String[] encodes(String data) throws Exception {
        return new String[]{kmsClient.getServerTag(), kmsClient.encode(data)};
    }


    @RequestMapping("encode")
    public String encode(String sessionId, String scene, String data) {
        return kmsClient.encrypt(sessionId, scene, data);
    }

    @RequestMapping("decode")
    public String decode(String sessionId, String data) {
        return kmsClient.decrypt(sessionId, data);
    }

    @RequestMapping("signMessage")
    public String signMessage(String sessionId, String scene, String data) throws Exception {
        return kmsClient.sign(sessionId, scene, data);
    }

    @RequestMapping("verifyMessage")
    public boolean verifyMessage(String sessionId, String scene, String data, String sign) throws Exception {
        return kmsClient.verify(sessionId, scene, data, sign);
    }

    @RequestMapping("derived-key")
    public String derivedKey(String sessionId, String keyIndex) {
        return kmsClient.derivedSecretKey(sessionId, keyIndex);
    }


    @RequestMapping("encode1")
    public String encode1(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) {
        log.info("硬实现：encode1:APP-ID:{}", appId);
        KmsClient kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/auth").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.encrypt(encodeReq.getSessionId(), encodeReq.getScene(), encodeReq.getData());
    }

    @RequestMapping("decode1")
    public String decode1(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) {
        try {
            log.info("硬实现：decode1:APP-ID:{}", appId);
            KmsClient kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
            if (StringUtils.isNotBlank(appId)) {
                kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/auth").appId(appId)
                        .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
            }
            return kmsClient.decrypt(encodeReq.getSessionId(), encodeReq.getData());
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            return "";
        } finally {
        }

    }

    @RequestMapping("signMessage1")
    public String signMessage1(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) throws Exception {
        log.info("硬实现：signMessage1:APP-ID:{}", appId);
        KmsClient kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/auth").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.sign(encodeReq.getSessionId(), encodeReq.getScene(), encodeReq.getData());
    }

    @RequestMapping("verifyMessage1")
    public boolean verifyMessage1(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) throws Exception {
        log.info("硬实现：verifyMessage1:APP-ID:{}", appId);
        KmsClient kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/auth").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.verify(encodeReq.getSessionId(), encodeReq.getScene(), encodeReq.getData(), encodeReq.getSign());
    }

    @RequestMapping("derived-key1")
    public String derivedKey1(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) {
        log.info("硬实现：derived-key1:APP-ID:{}", appId);
        KmsClient kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/auth").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.derivedSecretKey(encodeReq.getSessionId(), encodeReq.getKeyIndex());
    }


    @RequestMapping("encode2")
    public String encode2(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) {
        log.info("软实现：encode2:APP-ID:{}", appId);
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.encrypt(encodeReq.getSessionId(), encodeReq.getScene(), encodeReq.getData());
    }

    @RequestMapping("decode2")
    public String decode2(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) {
        log.info("软实现：decode2:APP-ID:{}", appId);
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.decrypt(encodeReq.getSessionId(), encodeReq.getData());
    }

    @RequestMapping("signMessage2")
    public String signMessage2(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) throws Exception {
        log.info("软实现：signMessage2:APP-ID:{}", appId);
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.sign(encodeReq.getSessionId(), encodeReq.getScene(), encodeReq.getData());
    }

    @RequestMapping("verifyMessage2")
    public boolean verifyMessage2(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) throws Exception {
        log.info("软实现：verifyMessage2:APP-ID:{}", appId);
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.verify(encodeReq.getSessionId(), encodeReq.getScene(), encodeReq.getData(), encodeReq.getSign());
    }

    @RequestMapping("derived-key2")
    public String derivedKey2(@RequestBody EncodeReq encodeReq, @RequestHeader(name = "App-ID", required = false) String appId) {
        log.info("软实现：derived-key2:APP-ID:{}", appId);
        if (StringUtils.isNotBlank(appId)) {
            kmsClient = KmsClientBuilder.builder().kmsUrl("https://kms.transsion-os.com/compatibility").appId(appId)
                    .kmsPublicKey(serverPublicKey).publicKey(sdkPublicKey).privateKey(sdkPrivateKey).build();
        }
        return kmsClient.derivedSecretKey(encodeReq.getSessionId(), encodeReq.getKeyIndex());
    }


    @RequestMapping("derived-keys")
    public String derivedKeys(EncodeReq encodeReq) {
        Map<String, String> map = new TreeMap<String, String>();
        map.put("algo", encodeReq.getAlgo());
        if (encodeReq.getAlgo().equals("0")) {
            map.put("dPub", publicKey);
        }
        map.put("kLoc", encodeReq.getKLoc());
        map.put("sc", encodeReq.getSc());
        String keyIndex = "";
        for (String o : map.keySet()) {
            keyIndex += o + "=" + map.get(o) + "&";
        }
        keyIndex = keyIndex.substring(0, keyIndex.length() - 1);
        String encrypt = kmsClient.encrypt(encodeReq.getSessionId(), "default", keyIndex);
        Map map1 = JSON.parseObject(encrypt, Map.class);
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("encodeKeyIndex", map1.get("enMessage"));
        objectObjectHashMap.put("mac", map1.get("macMessage"));
        objectObjectHashMap.put("randomNumber", map1.get("randomNumber"));
        return JSON.toJSONString(objectObjectHashMap);
    }

    @RequestMapping("getPublicKey")
    public String getPublicKey() {
        return publicKey;
    }

    @RequestMapping("signs")
    public String signs(String data) {
        return RSAUtil.sign(data, privateKey);
    }

    @RequestMapping("verifys")
    public Boolean verifys(String data, String sign) throws Exception {
        String serverPublicKey = "lhJGMB/jCUXEScw0DpHWlji8nX28/BuuWSds8pUYc6bmmHG3+5yWbGdbo9DQmkUyPIkIRdAl/lEoub51Hjjd1+zLwRrV/LqoIKw42e7h43ClhSOm3LJeFu+kHvlTxclPVqV+nkffTwoSQrYKW1p3tC71fFDc8jfaGuD5y+d+JRsbMNai3yrPz9qHmgjxTb9k9q8WKATi3VLI8tAXQqcIsycBDB6nicvHxSWrr7VjiRXBY8a0vF2rCeTYkuY3q/SMlNutOwlW17cqZyR/RNhDM6v+2vrp1wAGOlz04rYstShpGwJQajSPVh0YcoUSOK921+j56lmmY5gXnfSNsj514svNO08fcjPVDZa/EWEu8cXaVOh7igReHOnQtJyS2E6KMbTuCFnI5URa8W92diEXUqit5I5fybBGZEDOpivCVDzrQV5JD1rN4goQwNzoNKrjSxi1E5047Ds8WGbYo8hsbYWms6s6/LKIP2lz8TyIxqmUwuh0R51rz5VyPVaW3Z6nOqhjskEB7y/b8h7bfnpObw==";
        RSAUtil.verify(data, AESUtil.decrypt(serverPublicKey, "6XYVtyvgwyT5TGc0"), sign);
        return Boolean.TRUE;
    }

    public static void main11(String[] args) throws Exception {
        String serverPrivate = "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";

        String serverPublicKey = "lhJGMB/jCUXEScw0DpHWlji8nX28/BuuWSds8pUYc6aOuEYChm3Q5d4yc4GupOCN4a/66FeUWiGClBy1BHnIts8phvDGRrPcptsU9vFH3uMKx1uNj5OGkNZ+cB4skIqC3CHOoKw2euwRCyiNP1r+emb5xZpwt9aX0+oyBeRdvTPkhJ1mbDEHTHCBAz3AfOh9XvzIRreTh3jVg8cMA74/g4C2wm+xoZ6fH3mTLqgr9yTjliW6kFTnLutfAuxgHRsDzeBnGiPjLiYzCEASi448JAU9LoyJL2Fqgt8xQC65k5O/e5Q7RxxxQvPR6mCb+07GWTlgIbE8EF5DwS+KKHT2tovatcj5yGVSyOzABT12Kfn98AjqTikLBksTp0uw8lgH6TcVEUrFozgpiIap0uS6jhrRIzsdHWdrlJYp2QC+GCc16vpGiAsR0A6OU8n4nuz1yswKxH8Ax/5TLAjlGei4ziYhSUnME0oNM8e9BXXYR2St042OeW1eXVb+NSGjbYMNulVWcDgz/WL71exdFXcEEw==";

        serverPrivate = AESUtil.decrypt(serverPrivate, "6XYVtyvgwyT5TGc0");
        serverPublicKey = AESUtil.decrypt(serverPublicKey, "6XYVtyvgwyT5TGc0");
        String s = RSAUtil.encryptByPublicKey("qazwsx", serverPublicKey);

        System.out.println(RSAUtil.decryptByPrivateKey(s, serverPrivate));

//        RSAUtil.decryptByPrivateKey("d7E\\\\\\/QhbH8qFRmwL8lg97rg==", serverPrivate);
        //        serverPublicKey = AESUtil.decrypt(serverPublicKey, "6XYVtyvgwyT5TGc0");
//        RSAUtil.verify("123", serverPublicKey, "X2N/TPgdDIP6IPmjyaqgiAivVxwS01zXf9Vkww5IKYVu6YhpumLILeVPt+nx7Uvrydk0f6mgsapRPK0bPQHmlzrr60zJDhMqW5CqX5udB2ltyu1DvCWAZiWp8xxWD1qeJDdTp27lX1QfIegS0yTjzd9NQPWK9LMRzplkWTGbBykeGJwD4XhABwhAikGtryzdPrKkeL9BcKxF88TBQug2RjSuK/2lT2KEZ1l7RLAkE0ritxq95gNo5VivgUAvGbdZAcRvlifD2GI2SJ+EpT7sKDdcIorY2ae0BZ+SNrlWBXInOQpdTonP7c1boI4dWtGCAuMv3NvoMiq6gKW5DeySpg==");
//        System.out.println(123);
//        System.out.println(123);
    }

    public static void main(String[] args) {
        KmsClient kmsClient = KmsClientBuilder.builder().kmsUrl("https://test-kms.transsion-os.com/compatibility").appId("oemlock")
                .kmsPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgF9tqNfH49jU+cxmVAArcyYoA1ZTiz0ZBPTWa2B77L1AKW9u6aMtCCJuSo4nuw+jYNKirFAZ5soaTV7RF684oggXyBa+NeOU/0tYPhJB55YOcrO6FTJy3dKyw22tM/cUkMXhFvCK3K2iOCeEn6skIs7qsMXa96qZAKaQ2rnX9mn5ieLt/L1pjphc9pi6cBx9oIftLW0/I6jZ5MxGH7eJYI6VfjPZDoqRnLWoShvOLSCJA6bYyL4/iYiANEB82K3Zy3vAc137emt/OP2HLchVrr9qCeZyuVcV2eN6j4yo0XhfZ2NcIOJSFt8XlUzd2n6Q78pmLO9QgoRDwxlIOKlHaQIDAQAB").publicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjS7Q+PeJ1Yog95QIokn1H/lrP4er/49pshhnne0l7vY8YpsQ+6z/kFNiR9VfQR4Zg7BFw7EV7gAj3gdxlou833eZ/JdjaX/+yHqsh7yx9/3xmUFxznsyVeaG7xLa33tQAIrylFtETOYsmoR7wRv4mukvOk13kdjBeq34LOgIyhkKbVx9kQIPazPfxcIaBc5IuQ2BfAao0MJWYcofTg8ZZiy7tabfDfxoNOlVrUXQyGs1Er5Kjg5VoWEPZ2ZykLfJ4GMevSM7bs8p7yMtFa7K2smgs3HdY6mFVJKaOFcjJ+Ohy47sAeFoVM7ozu5cQQZwpeSrhBTX+t8wUgnpOoHn3QIDAQAB").
                privateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNLtD494nViiD3lAiiSfUf+Ws/h6v/j2myGGed7SXu9jximxD7rP+QU2JH1V9BHhmDsEXDsRXuACPeB3GWi7zfd5n8l2Npf/7IeqyHvLH3/fGZQXHOezJV5obvEtrfe1AAivKUW0RM5iyahHvBG/ia6S86TXeR2MF6rfgs6AjKGQptXH2RAg9rM9/FwhoFzki5DYF8BqjQwlZhyh9ODxlmLLu1pt8N/Gg06VWtRdDIazUSvkqODlWhYQ9nZnKQt8ngYx69IztuzynvIy0VrsrayaCzcd1jqYVUkpo4VyMn46HLjuwB4WhUzujO7lxBBnCl5KuEFNf63zBSCek6gefdAgMBAAECggEAdmW8eg2dWabs3rMWSoMbPiU8+5obc8YhLWmp+5bVKQD2wvqn02fRKofC89rMdx925ij9WI85hM45RgmYCFhHnGAAkwOiKudcE2RNK8fit8YOxidL+Yz3OXKJ1hRJ2QDgppPfuuqO7K5a2XohDmEWq1LCeHJew8CzAN7EMWSlelUmTOUIsXEE2ryVYkGmaUt/Xm9E8kdtcTXi9hoYiIMWtUeTV7thou400S/qmMIOcQAN05UIwCBu0SLadWwKm7IGLQn0EWaRn3pKM/ajIx6JtnROFqr+LuDCBzsXl9HTj5qMRQk951rfcJGx9Xgfulrh3VP7tP4IVMIz5d0qgW0wAQKBgQDISRX58Ged28we2gybb9WLEFmU0pyJ7+52wFnvBAzKUe654rdtV/PKWUSGUgisQqNDhiJ1IS55HY4qOuslFGb5B4JehCCngJYjN3qXKQSOeAZc4fmxYvmYgQoP73+gmRvWyq13HCR4qK+nDKMV3lT8xBpgPAyMurS8mMVFsxZTwQKBgQC0dNxcnp7PtRv1isqLk7oZDql5lcGd1PZcLw2YikWJK8z78IdJU0lFGWeRiH4Fp3lly1jZ33Q/2h5DMC1/FaIdsFWtr+fPAJNOywgYmWkdNdUOQb08WkYEo0DZyoyhxyGxEevzhBEeuFbXmWgEpCcl9o5VE75gLgG4U09xhykrHQKBgQCHePAVfGgJ3cdGwwSuCsotYyTExmS95H2u81KhmmKwYxUPxOt/WXN8Vj9Ef66AHh7iqJMcGRKfN7+Wjv3IufEg1tR26/ZOdmqXbYpNPqBnYYYkQ3Gqa6EaZ4YP21a/oXC0K6mDnJSKzMbrPl/9hcKEqnyQxW1Tr2JbAxbEm2spwQKBgC+WveJDBniaN2RalK2YaLRg9HqW7rdzyQBofDplBI93GJi8RsEkkKcDWxe7GhjdoJsitCzIy/xmxiJL20OvbdBMh1vFjuk9KpvV2S27j0ffwW8AESqQV/SzJNAKpWmofQ8YPdQ7WNR0Ipq63v6SV6ygR8rR6RuuJk/40HONBGNVAoGAS79jzt8skvR6yaJcGWN5iY8mhCtttkh97xIrZQx7sUEjF+RsEWBFsE4PtdlQY0fM+wnNEufrIfJJ3IpXdJIxsg7fIyhapBiYIR+t07v2eZqw7CHQoIR+bmhw8OCUx2NLAO8XD8vI1YIUm8ntp5vzuLd5H0Zt5Sg06pcrhQvIHeA=").build();


        try {
            String decrypt = kmsClient.decrypt("8D14D1DE895E2519C1059278E66F9AC183701E7DDF296F2B647C3EED2B13DAE7",
                    "{\"enMessage\":\"RhMY4QDWcH6A\\\\/LxHEQ3yQqO41i\\\\/ulOiKgiw5GET3lb+IcYU5z9Cr\\\\/7G6zpQfwoPzglgynsgDmYac91B0WLwLtK5WHSbk0RylVpkmUeEl3rD69s04yiCczTB3LNvorqaIHgxoBFCiTfTyRDGpip98J9pM1i8Ut+5Dw5KRum+F66CEmwtuJgt6VF0hg5UGLy3Z8sbFiQON8Z+GL1Nz9jgDo79mGtVJSNIbOyOuGXHg5zjd07fHRLx4nAi1NWH7WJ4nZ\\\\/1UMv6SQCsnXQb0nNybKc35cj1lX\\\\/C8fDSYvWZGkTjYdtWe1Bjz123g7nWrEQ2WSfxtSfEYl1op6oJoBbOmt48OpitMAFJaq9vQYdAw+QX1nfw9vWEZeddC77hsVZbKjmRJZyF+Y\\\\/pYUOHLNBv0xC8IZM+UQBzDeLZfaCv8vFCCjhRwnuMZsZPccmtwUYF\\\\/lwaZdnTK8vcxQV3Vwi\\\\/5fQ9s9hMFR79qHBCf12Fn7OsfN7FUYBKz0xhZtgvTkTp9JlSPSgMU13Y10R603TxVhKpHqNOe+8yDmwiotjL9hCLi2x98FmWpCcQsbUvigijVVSCCkTfSbLHxM9rIEZraus9wGDCQCcJG5Q1WWowJR8Tpbcq43R7xqZ9snNUlHniqKEqBSNf3jtUF3ccKFDuBjT0L1IrniVpWjTcKecDwT65UvGag7R9Vg3kNl13ZFqoOKKRQlKDmHU\\\\/D8Et2igCuhnmerAeo1YaF6PQUGSMp3JhaVCqCAtejG99QRfgb\\\\/astWGSXdn77RTmWAh7YTDkYMfRqvmXZ1OmiVO6xo4KFeJAWidxfHmRo745sFWF5GojZCqZqO\\\\/jxD7RT42TVI05wRtNoLgMrtonlsWXdEpfF7lawJgL0rGR1IWnZI93hJQz4ujNdLtOJMvrLbeM6dpFrA9dy4IUa62dCla5MlhOfSkugkXpJw6zbCHWM5NU5yxSld+6m3dPsh5kervk69iz2QmPcsEipOIffZrv88lSdJubnkpG8NMOb2dxzTZ3t\\\\/8dPEnCctD8TANDf91FFUNDHv9CL02dsk\\\\/gOIDtnEdod8Zs3zdAGUchPQEA8gQTYeVTdxRvjKsXhFmItVQ0Zc8SOMXBTDZov+3T\\\\/lss0XczSaqbkirz0JwGrmM6G+mK0+WLnZojbkm2wbKOzuhCdjfbRZxljVidf5MqCq\\\\/SeOaixbzljYg6ELPJ7Cpoz0ZSyUnLW5DyRHoo58B4lICf\\\\/SlfLNJlOd3O9Rwqe+kRnDoTcI2ui\\\\/MoJbeIsDs4mHCxevF7cPyFcWkBPiZISE1JEPrlMqb29kgYweyZD4nBmxneBAcguHtk9WoUmynuca2uQjjOOQUQd99eZiy1AnB7wq3QJsY0JMwVOEGBSc2j3U9rkflLiswjDF9NDWo4HfPCAM4rdzLjYLMZNBXd2ZfBVtwkQ4CV4o7lgVIQ+NHs9m0aBy9eFYEslqkBgfoHQaIB7obCrdO2FZjy5SXF73oXCAwjF8GtYXakMDg6ZBmwMXiQtmPJwvLLyQ+odyC7Bfs7heK42FqrkMMKtyej1BU0kA1+8zKmn8EZOeihV72iLc+bP7\\\\/2sAiWxyx+DcYaG14c+kV7RafDgh3aicPyEkT6u7o7Dt9\\\\/PqY1oCX+1H7KeiE2HUGjBtpfR+vMOTLqwnQlTbcyANYK8zw6gtVStq8XNUQOtcaX1aKbhfhrrXOPbkTyrvALJ\\\\/NocH\\\\/XjFWXkMyPkZ6Ctfp3qisLI07ym47Q\\\\/RvFDhW8q0pZpbC+AjIKq17+J1QKfyL7cqZt7JwgnEuwPkY0iGpnpgFAOfimIImuLNLrzJJ3FquZzksVC6XI8JvqEERzN8DMHQn7ydAIMPvQG1uP8RZrX\\\\/k8XNHdebE7FxzjAPA==\",\"randomNumber\":\"     k ʨB | \\f  \",\"macMessage\":\"YZmcFEXP\",\"scene\":\"default\",\"sessionId\":\"8D14D1DE895E2519C1059278E66F9AC183701E7DDF296F2B647C3EED2B13DAE7\"}");
            System.out.println(decrypt);
        } catch (SdkCustomException e) {
            //
            if (e.getCode() == NetCodeEnum.STORAGE_KEY_PAST_DUE_ERROR.getCode()) {
            } else {
                //抛811 就行
            }
        }

    }

    public static void main2(String[] args) throws Exception {
        String key = "lhJGMB/jCUXEScw0DpHWlji8nX28/BuuWSds8pUYc6avG7UxbXfPN3OFuhDsAeRHH/oMm5K7LZFjyxEAP7DwaBZ0VcubwNwcDP3kjIDRBkyuBTs9Hh9IhcIZ7e67yJTKOK1nL/BaDdlcXCh2Ud6ZeliVqCEpeZyZCgoS716KE/c3Z+MbwGKphN/8k2T+3dvMlNiEwxZnFzyjdT/hFGTFuOcU4RBirx9DsUYqKlFgwAu9dc2lh4YVHVgm7g/lsRgRXM/+Ejd6DjJouGLdPZMuzl17Y39fyGh0qIFd3U/WQ+pePyTIFR+LLCGHgrh5UEklKqB9g7OiWw4X/Z1YIxiqImjv4tlLbzscNZuO/YJ1t5pR1JpOBsCMo9fOq1Q9MKkuGogzQWBrBggFNMtmpJOho49gBECr4C698AXfeEB5IODKQfLsJEgbB4drs68spYtsm+GW7YIYY5wXTo/YpkCV2F5qUulCs31bwFFZj/88yphv5qeJXIAULx8wcXNTlD0P8GaPDYpiM4icBqtIQHQqAw==";
        key = AESUtil.decrypt(key, "6XYVtyvgwyT5TGc0");
//        ahKy/O0Qvg2CzQ+t
//        ahKy/O0Qvg2CzQ+t
//        ahKy/O0Qvg2CzQ+t
//        服务存储密钥获取:6XYVtyvgwyT5TGc0
        String key2 = "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";
        key2 = AESUtil.decrypt(key2, "6XYVtyvgwyT5TGc0");
        String s = RsaByteUtil.decryptByPrivateKey("Etk2t667ah70IKWe2M5oWBQRYw7gt10hGEGnhBhnTW8otn5xrFA5rhgHL/KvWYAhzJ456TogejZ0" +
                "adQP5HJWm9XAKRb/sQ66pAPTr+jo9hemLgrnoPulfYFjEHtTxr2xDlNcrWeta6ZK178vuRZQFEaw" +
                "pf+IVPk1Q0Le/HIXSzzr+Z62WFSQz+oP4ceNG+wf4RnDKtd2WcSPlPf9Mzyu1mVdY9eFXTB2s9Gc" +
                "BghHwG8T106soKn161EkfOaTgMAdEgLT5jZFDPS2IWQSMXX/ep5xXHcnfmitXRCC2907JVg0fXTX" +
                "R9SNtUp7rG2ihO2bCAysNUi36WYl4BHwmwW0jg==", Base64Decoder.decode(key2));
//        String s =   RSAUtil.decryptByPrivateKey("Etk2t667ah70IKWe2M5oWBQRYw7gt10hGEGnhBhnTW8otn5xrFA5rhgHL/KvWYAhzJ456TogejZ0" +
//                "adQP5HJWm9XAKRb/sQ66pAPTr+jo9hemLgrnoPulfYFjEHtTxr2xDlNcrWeta6ZK178vuRZQFEaw" +
//                "pf+IVPk1Q0Le/HIXSzzr+Z62WFSQz+oP4ceNG+wf4RnDKtd2WcSPlPf9Mzyu1mVdY9eFXTB2s9Gc" +
//                "BghHwG8T106soKn161EkfOaTgMAdEgLT5jZFDPS2IWQSMXX/ep5xXHcnfmitXRCC2907JVg0fXTX" +
//                "R9SNtUp7rG2ihO2bCAysNUi36WYl4BHwmwW0jg==", key2);
        System.out.println(s);
    }
}
