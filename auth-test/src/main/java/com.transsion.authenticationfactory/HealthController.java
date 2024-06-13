package com.transsion.authenticationfactory;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.transsion.authenticationfactory.infrastructure.advice.CommResponse;
import com.transsion.authenticationfactory.infrastructure.constants.NetCodeEnum;
import com.transsion.authenticationfactory.infrastructure.exception.CustomException;
import com.transsion.authenticationfactory.infrastructure.utils.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

/**
 * @Description: K8S 心跳机制
 * @Author jiakang.chen
 * @Date 2023619
 */
@RestController
public class HealthController {

    // 加密机公钥私钥
    private static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx7SbUzwIeJ4nV8zfML7S66hp9O0k2vwApRtsc/y+jdW+Np3ttn9uqJhuTANKfAZw1DbauBbc7YcCDdEUzdlGQcTsvoHCLkm64EZkh6WvvZv4Gq2BCOmeK8M+TAjY2SvYm7rFQ6314WovxcSvfqL+DL17uU0VyFX5oLYtBsiZtv+07lmn6vYuunrQh2AFumJqtywnRf9QgoKxVxsaAUuhtBo6H/y5FiI9pLtGf1nNgbEpJ9QLL/TuqcvewVQbkGjlpMkMdHa/rVwWrLp22jCQusrtwMqgQkPwTqw9+KPol0z7g7oEVFmxiPVIsgRzT+NI6IgYumXhfPBjnBtoEfEp3QIDAQAB";
    private static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDHtJtTPAh4nidXzN8wvtLrqGn07STa/AClG2xz/L6N1b42ne22f26omG5MA0p8BnDUNtq4FtzthwIN0RTN2UZBxOy+gcIuSbrgRmSHpa+9m/garYEI6Z4rwz5MCNjZK9ibusVDrfXhai/FxK9+ov4MvXu5TRXIVfmgti0GyJm2/7TuWafq9i66etCHYAW6Ymq3LCdF/1CCgrFXGxoBS6G0Gjof/LkWIj2ku0Z/Wc2BsSkn1Asv9O6py97BVBuQaOWkyQx0dr+tXBasunbaMJC6yu3AyqBCQ/BOrD34o+iXTPuDugRUWbGI9UiyBHNP40joiBi6ZeF88GOcG2gR8SndAgMBAAECggEABHkrMYYaunjwcKsKJNFhcB3poa7eMHmMmiYvG7oBmrO6+wY2mOvp0qT3c4RvWq/aPyarZ4w7jqaDiUOxr5q6VCbRFsNZUigco4FwJwmzEhA4UoCob0asYFaTQqjAEr1EsNhSKt3usAns4AUz2SZVgnuMFe6AGmiymsL55zj/GHY/uLsORfqxg0A3QVToKRPGMlWnJQRbv8MGSm1Ss5Ldhug8BclXPxLmadNAFvIX4BxWPrHLSH2Y0QQmr00hg7YscL9G5eW3qig3W9SbvyMpVNqHidLU3DSAoEBtVX/uoYKqONqa9FvhqGj6Xb3tIa7u/Gl4ulebzVx8Y/ncGjiVkQKBgQDpmG6WpWFG7CXK/jo1/cWDq8vUv6X9XLRmc6Ujcznvx7woYxH6ovcV9cm8Rxzzxx4HM6ugXG9qtbDhJKmfKgjYNtt8w8/yfWkaS0bsd5treSJVFGzc9r4zkTCB7o22LfoMZwNUqXAq02fsDFpuLkkPnoNRp+6QMz71m9+qCvoUuwKBgQDa3A+DfTeFz2z/r3wME9FYYIDZYinCCWuUk09mtCk4Paxhmn/Wyq9uAlvuSJkh47Fp+FYcTXBcb2cTVNnG4/z5Y4OYjaq24z1b78G9WbR0E6UKT+BvD0uyJTv7LiNtc4+q1h30581FyVJIpDfRlK+7JaqYXnCqZ6MsWdMYsb6eRwKBgQDSUgOoMak/URnKBlJlEGNI5WJ14ERUtaZb5F3YaEPO55g7CNBESXyM0itcEO85cptPwq8RiIhsnMBwRjFO9GrYObQvRqKxY0dBfGuUNzDBeGR4FBbccHDaPyxcoGwJjqXnisyN6qzqckPMXWcCczwnCwKTf88lGG7kdv7PvGhkRwKBgFH7HqCNdg/cnCLGtDu97KwjMgC74ZJ6ft7fs11wXQRhLcI94mct8zrNDWybqyjjpqBgUrk1VMJ7RGwcW5tB6Y/tc+zgHDN3uZNHoxXvlUXp2eeb9iED5MHtaqNzK/gi0o0WuY/0iJL8j6MASJxsCsual2WN8rwSKDBD0WwtoI6fAoGAbBNcRkCDJVi76AGPJ7w1IpcyBh8AruQQAC2ky1eWyBTqQtIUq5xi0OH+H4FVCLIIoWqvaHTXwQNRPaWMB8jZSppvCES2+R0kwblVpt5rtsNcm/gAjD705YHumxbJQNUI7KJw+sOLmyNuZIY633Wyc+GOgpgy5Rh3UlsxPBZMkAQ=";
    private static String rootCert = "MIIDxjCCAq6gAwIBAgIGAYk/AwYTMA0GCSqGSIb3DQEBCwUAMIGDMSMwIQYDVQQDDBpDQSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTELMAkGA1UEBhMCQ04xDjAMBgNVBAgMBUhlTmFuMQ8wDQYDVQQHDAZBbllhbmcxITAfBgNVBAoMGGF1dGgtY2EudHJhbnNzaW9uLW9zLmNvbTELMAkGA1UECwwCQ0EwIBcNMjMwNzEwMDc1NTEwWhgPMjEyMzA2MTYwODU1MTBaMIGcMSMwIQYDVQQDDBpDQSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTELMAkGA1UEBhMCQ04xDjAMBgNVBAgMBUhlTmFuMQ8wDQYDVQQHDAZBbllhbmcxITAfBgNVBAoMGGF1dGgtY2EudHJhbnNzaW9uLW9zLmNvbTELMAkGA1UECwwCQ0ExFzAVBggrBgEFBQcJAgwJ5Ye655Sf5ZywMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx7SbUzwIeJ4nV8zfML7S66hp9O0k2vwApRtsc/y+jdW+Np3ttn9uqJhuTANKfAZw1DbauBbc7YcCDdEUzdlGQcTsvoHCLkm64EZkh6WvvZv4Gq2BCOmeK8M+TAjY2SvYm7rFQ6314WovxcSvfqL+DL17uU0VyFX5oLYtBsiZtv+07lmn6vYuunrQh2AFumJqtywnRf9QgoKxVxsaAUuhtBo6H/y5FiI9pLtGf1nNgbEpJ9QLL/TuqcvewVQbkGjlpMkMdHa/rVwWrLp22jCQusrtwMqgQkPwTqw9+KPol0z7g7oEVFmxiPVIsgRzT+NI6IgYumXhfPBjnBtoEfEp3QIDAQABoyMwITAOBgNVHQ8BAf8EBAMCAbYwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEANydiaP5ETNPv7x/43ico8oBYuiML+yr63eGHN/sEqNgqIl9A0mIJnlRBNhCvImL7in3/pd1Ujz/IB10tJVcnNo83aoUWWNp3R0rweEDecv5ffRtoEu+aXMKnDRLYucNAX+LGDzeuOMiLb1Pb1AMP4knn45S7G+MdIfIfz9Wdp7KIWJHj8EhueTKvffFwWNzAEKr4YLXFu8KazmeGXz63L4F8DE5dGuoZ7o2pm/tMcijxUJ9k5aK8YMC/8Fx7TVul9nqjh75cATpZdZO7cH4EY4ej2F8INm+TBw8PYu7HFMYUkVbizvHxZmrWh56ppzS3cPlb/b/2AceEcl94PfhN/w==";

    @RequestMapping("check")
    public void check() {

    }

    public static void main(String[] args) throws Exception {
        String csr = "MIIC9TCCAd0CAQAwfzELMAkGA1UEBhMCQ04xETAPBgNVBAgMCFNoYW5nSGFpMREwDwYDVQQHDAhTaGFuZ0hhaTEdMBsGA1UECgwUa21zLnRyYW5zc2lvbi1vcy5jb20xCzAJBgNVBAsMAjAwMR4wHAYDVQQDDBVLTVMgRGV2aWNlIExpY2Vuc2UgQ0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCVz/H1ZNv9xeNGXCO2iXPLj/uVI8Hkt07Bit6J+8DRv5NQuSdrZ/wBusr6CY0RrtQ5zgkPGHQ39wlWrgyNGNQcOZkP9DnC6GWdi4UHEmjcHZeXSpaxRLb8+dJseL1i+K2GsrF69ViaJCjuToQ0iLZHlaSo1qprKU6LLQMqCOWJkbHkRaAlxW+W7/VPUwPQzPsjafbbwg4kumbVXFV85DJ6POLjvEjsI+w9QanHeXE2E2oWzMWxjYFshRW1VqQXXDcZI1k30PPdL0PbVY4k4rtpZfRjo3CVseCLRUYz15YG+/kxyoZnz1QDx1AhTTzrbdunMRjgVtfRNml2zlom/iyNAgMBAAGgMTAvBgkqhkiG9w0BCQ4xIjAgMAsGA1UdDwQEAwIClDARBglghkgBhvhCAQEEBAMCBeAwDQYJKoZIhvcNAQELBQADggEBAFr9MMn9MNlKcagadDvqYjn2GtmUXpPiN69F0khkPtd1KCGEU3hyJHaRf6C1Gt7YHF9wTCWthYNsst85JczXIiwI7ETKDDiYK2SZxj1iCjXIq8Z7hxdOQ0+7uY+cXlkqZAhuQW94NHYDF2ZAJcc4hOQi1/2jtPCBy5PzXPagp6iKZrBAfrXZQv8uGhNVmdxlr1FN+nQ6Sjsn9oFtoJ+/xqd+uil8iBEy8JtPuxz5fIaJGYJEQ3TI6jwvkbJwd7fDqFpFYc+cT6wc1IFwVOUf6FnN1l9qNPVryQU5HF/1IJ5IAbx8YKSpN4RVUI0DHEyFHESNiDYuYKh4NQLUWq";

        String factoryCert = "";
        String deviceCert = "";
        X509Certificate rootCertObject = CertUtil.getCertByStr(rootCert);
        X509Certificate factoryCertObject = CertUtil.getCertByStr(factoryCert);
        X509Certificate deviceCertObject = CertUtil.getCertByStr(deviceCert);

        rootCertObject.verify(RSAUtil.strByPublicKey(publicKey));
        factoryCertObject.verify(RSAUtil.strByPublicKey(publicKey));
        deviceCertObject.verify(factoryCertObject.getPublicKey());

        CertUtil.lf(rootCert);
        CertUtil.lf(factoryCert);
        CertUtil.lf(deviceCert);
    }

    @RequestMapping("/key")
    public CommResponse key(@RequestBody Key key) {
        String data = EncryptionUtil.decode(key.getRandomNumberStr());
        Boolean verify = EncryptionUtil.verify(data, key.getRandomNumberStrSign());
        if (!verify) {
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
        ServerStorageKeyEntity resp = new ServerStorageKeyEntity();
        String sha1 = SHAUtils.getSha1(data).substring(0, 18);
        resp.setEncryptStr(EncryptionUtil.encrypt(sha1));
        resp.setSign(EncryptionUtil.sign(sha1));
        return CommResponse.success(resp);
    }

    @RequestMapping("/cert")
    public CommResponse cert(@RequestBody Body req) throws Exception {
        CertUtil.CN = "CA Certification Authority";
        CertUtil.ST = "HeNan";
        CertUtil.L = "AnYang";
        CertUtil.O = "auth-ca.transsion-os.com";
        CertUtil.OU = "CA";
        CertUtil.PLACE_OF_BIRTH = "Transsion MES Factory";
        String csr = req.getFactoryCsr();
        String csrSign = req.getFactoryCsrSign();
        System.out.println(csr);
        System.out.println(csrSign);
        String data = EncryptionUtil.decode(csr);
        Boolean verify = EncryptionUtil.verify(data, csrSign);
        if (!verify) {
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
        RSAPrivateKey rsaPrivateKey = RsaByteUtil.strByPrivateKey(Base64Decoder.decode(HealthController.privateKey));
        X509Certificate x509Certificate = CertUtil.generateCertByCsr(data, rsaPrivateKey);
        ServerCertRecordEntity resp = new ServerCertRecordEntity();
        resp.setCert(Base64Encoder.encode(x509Certificate.getEncoded()));
        resp.setFactoryId("工厂 ID");
        resp.setFactoryName("工厂 Name");
        resp.setFactoryMac("工厂 MAC");
        resp.setFactoryIP("工厂 IP");
        resp.setFactoryPoint("工厂 point");

        return CommResponse.success(resp);
    }

    @RequestMapping("/cert1")
    public CommResponse cert1(@RequestBody Body1 req) throws Exception {
        CertUtil.CN = "CA Certification Authority";
        CertUtil.ST = "HeNan";
        CertUtil.L = "AnYang";
        CertUtil.O = "auth-ca.transsion-os.com";
        CertUtil.OU = "CA";
        CertUtil.PLACE_OF_BIRTH = "Transsion MES Factory";
        String csr = req.getAuthCsr();
        String csrSign = req.getAuthCsrSign();
        System.out.println(csr);
        System.out.println(csrSign);
        String data = EncryptionUtil.decode(csr);
        Boolean verify = EncryptionUtil.verify(data, csrSign);
        if (!verify) {
            throw new CustomException(NetCodeEnum.SYSTEM_ERROR);
        }
        RSAPrivateKey rsaPrivateKey = RsaByteUtil.strByPrivateKey(Base64Decoder.decode(HealthController.privateKey));
        X509Certificate x509Certificate = CertUtil.generateCertByCsr(data, rsaPrivateKey);
        ServerCertRecordEntity1 resp = new ServerCertRecordEntity1();
        resp.setCert(Base64Encoder.encode(x509Certificate.getEncoded()));
        resp.setFactoryId("工厂 ID");
        resp.setFactoryName("工厂 Name");
        resp.setFactoryMac("工厂 MAC");
        resp.setFactoryIP("工厂 IP");
        resp.setFactoryPoint("工厂 point");

        return CommResponse.success(resp);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Key {
        private String randomNumberStr;
        private String randomNumberStrSign;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ServerStorageKeyEntity {
        private String encryptStr;
        private String sign;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Body {
        private String factoryCsr;
        private String factoryCsrSign;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ServerCertRecordEntity {
        private String cert;
        private String factoryId;
        private String factoryName;
        private String factoryMac;
        private String factoryIP;
        private String factoryPoint;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Body1 {
        private String authCsr;
        private String authCsrSign;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ServerCertRecordEntity1 {
        private String cert;
        private String factoryId;
        private String factoryName;
        private String factoryMac;
        private String factoryIP;
        private String factoryPoint;
    }
}
