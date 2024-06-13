package com.transsion.authentication;

import com.transsion.authentication.infrastructure.utils.RSAUtils;
import com.transsion.authentication.module.da.repository.entity.AfterSaveEntity;
import com.transsion.authentication.module.da.repository.resource.AuthKeyResource;
import com.transsion.authentication.module.da.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/5/25
 */
@SpringBootTest
public class TestRsa {
    @Autowired
    AuthKeyResource authKeyResource;

    @Autowired
    AuthService authService;

    @Test
    public void a1() throws Exception {
        String serverPrivateKey = authKeyResource.getServerPrivateKey();
        String sign = RSAUtils.sign("123".getBytes(), serverPrivateKey);
        System.out.println(sign);
    }

    @Test
    public void a2() throws Exception {
        String s = "123";
        System.out.println(s.length());
        AfterSaveEntity afterSaveEntity = authService.featureCodeAuth("93e286da49f398fefe8e7e01c5e870d9");
        System.out.println(afterSaveEntity);
    }

    @Test
    public void a4() throws Exception {
        Boolean aBoolean = authService.oaTokenAuth("r_OTg2MWVyNjRsYmg0aDNvMXczMWReNjE2Mzg3ODY0MzA5MzkyNjkzNDc3NDUx", "u_Njg2MWl1NXFrOHBlOHdteDd2dzFeNjE2Mzg3ODY0NDg4MDU1NjkzNDc3NDUx");
        System.out.println(aBoolean);
    }

    @Test
    public void a3() throws Exception {
        authService.palmIdAuth("eyJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjoiY3NNSy9zWFFMU2dsK1NDSU9LZFRYVzdWekRwOGZZdnpmK0gzSktTblNML1krd2IxYU9QZmtKM3Q0azlZTUJKMHM4amgwTEt2ckt3MXR1ZjVzVVNoLzR3emxrZlRvSXV6NEpNQzN1OFdGT1ZaMGpON0tPQms0RmdPcGMxQ0Y1a2psbUxXTG10OHBVUWJQVUswRzJTSkVNNFVUOVlMeForMmJrbkt6N2w5c0VEKzk0RmRjeThMNWRtRVFhOFlPMjlJTVZyMmJZY3RvemFyTzBzMkROUEp0OHlBWURMV1d5aEJIYUIxOHlueGRaYXJtSkhWN0N2NVk4NlFDTVZSZDdUbitVUWNjeW1pWlVRTEdsdDNxek0waDhGT1FNcmtmWlBjeUFYWHhLZHdDdjg9IiwiYXBwSWQiOiJkYS10b29sLWF1dGgiLCJzY29wZSI6IlNjb3BlLkFMTCIsImlzcyI6InhuIiwiaWF0IjoxNjg5MjE0MDQyLCJzdWIiOiJhY2Nlc3NUb2tlbiIsImV4cCI6MTY4OTIyMTI0Mn0.SfHBHmg29W_bJSe2AAVlyGPi4j0tEjI9LP04l0r077XUKsgpYUFPp2FXvKE2zOOBHMxh06RBEnzcYB_Hwfe6PeyEHUvFQBtoveKjZG5ZwwuNXPMlgphv6h38DYvcVZywzy9hiJPjrvDjm0FafiYgyRPpz962w7vWc_src6pBUGCSCiPIPSMmV8ubeGoq_u_dhZpSH78Y33j0i_wKIGbT2m-7EW5MPiMtYQBTuLXYmY1Jyay7xhvM_eQbhkytV8QO2F4OQ652C6OENzGw9rCLxsQS70Bym3pwGisUjoGOP5JkiustwJtiP97oo4iEDsq2N5_Q9wnVo_8WPWyBC8R3KA");
    }
}
