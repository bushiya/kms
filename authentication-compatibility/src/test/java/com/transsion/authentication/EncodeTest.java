package com.transsion.authentication;

import cn.hutool.core.codec.Base64Decoder;
import com.transsion.authentication.infrastructure.utils.AESUtil;
import com.transsion.authentication.infrastructure.utils.EncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/8/22
 */
@SpringBootTest
public class EncodeTest {
    @Autowired
    EncryptionUtil encryptionUtil;

    @Test
    void a1() {
        System.out.println(encryptionUtil.encrypt("algo=1&kLoc=0&kType=1&sc=testAES"));
        System.out.println(encryptionUtil.encrypt("1234567891012146"));
//        Boolean verify = encryptionUtil.verify("9819D201C4E2C5B862D1C118324D9A50B569A1B3AD20F8E11894C87A62C3D7D3", Base64Decoder.decode("shro1l3rXzVz24oWPkIv/g=="));
        String sign = encryptionUtil.sign("9819D201C4E2C5B862D1C118324D9A50B569A1B3AD20F8E11894C87A62C3D7D3");
//        System.out.println(verify);
        System.out.println(sign);
    }
}
