package com.transsion.testtool.module;

import com.transsion.authenticationsdk.infrastructure.core.KmsClient;
import com.transsion.authenticationsdk.infrastructure.core.KmsClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/9/7
 */
@Configuration
public class KmsClientConfig {

    @Bean
    public KmsClient kmsClient(KmsProperties kmsProperties) {
        return KmsClientBuilder.builder().kmsUrl(kmsProperties.getUrl()).appId(kmsProperties.getAppId())
                .kmsPublicKey(kmsProperties.getAuthPublicKey()).publicKey(kmsProperties.getSdkPublicKey()).privateKey(kmsProperties.getSdkPrivateKey()).build();
    }

    @Bean
    public KmsClient kmsCompatibilityClient(KmsProperties kmsProperties) {
        return KmsClientBuilder.builder().kmsUrl(kmsProperties.getCompatibilityUrl()).appId(kmsProperties.getAppId())
                .kmsPublicKey(kmsProperties.getAuthPublicKey()).publicKey(kmsProperties.getSdkPublicKey()).privateKey(kmsProperties.getSdkPrivateKey()).build();
    }
}
