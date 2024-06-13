package com.transsion.authentication;

import com.transsion.authentication.infrastructure.utils.RSAUtils;
import com.transsion.authentication.module.da.repository.entity.AfterSaveEntity;
import com.transsion.authentication.module.da.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description:
 * @Author jiakang.chen
 * @Date 2023/10/17
 */
@SpringBootTest
public class TestService {
    @Autowired
    AuthService authService;

    public static void main(String[] args) throws Exception {
        String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxRvZKFrPETugazAKb6RyuIWAd1UeK3dKIb7wjTp4y7DFdPmCjeNzaGBwMOTU2CCDWS2I7592vXw3zkYsf9jMmKU8YMWUz2ASP1QgNzXewasgrkE0sGygYJ3hE4XiM8bMdDR26rCUhDeCF/9guZUQsIcCiTZ3oaLED0Nj4WRRWwXex0pVS8Unm3929AloZ0BjS/y/xyvZB7geehDYh/lc4kjaQ2dAyZMjEwhLc0adDWEtjfGxNsxUS9gVTOld9pyD7BOTlqtbas7rNmTWcK0GOIGN073fzy0TJpvqbsUFPuDC7n6wEjEx5hKTlhAADSLqgEloyuloJAf9zcGs9FJuwwIDAQAB";
        String key2 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzkIvn+Cj9pRNpXLngwaQgCkFnvwkwiGFXEcKN0ENmsUA3cu70/ByCW9TnQfkCdeqeAwa5fDehYyU9bMxMhzKcJfpGqcCrGAeOzq+i1kuOjLxCB+2NOWw4W46KaJIpnwSwkh69bcMWXe9jBaVinnNQdGr7fKMvTieW/ZnJP1DeJAocFMtIErDvFep6ShJC1NmgjtJCUWp+bJJRR5GEBvUpY7Beq2YbHvYWAAtcMZchipacBcys9uUisyvc3H64aSiJjZZbM3VufEksxSkMJP94g8FxW+7jTLj5vFApZVna6dIqQ2+Rl2Akis8qiteGo6Qpv+SF1IH+yU7GrpJct6WLwIDAQAB";
        String data = "0a8b39b7f98369da772a7bb1e490584d0ecbb86303ddc7d0f7f6c86c5781f4e6";
        String sign = "CGZL8DoR4GpeU6E6+Z9M8BNJrpznbSX2bGsu4n3fXeqZSsrmN2rf8pg9aQejYPnGvD2Xpd+5GSmyPaS2QyLrmC8oLcNREWRy/lBw+m0p5CO20mAQ4RdzrgrbUxjwciXVymR46j52Hi9xEG4wogc2+SwQ4AMr/WIhw5AG/Qh0p5ehZI6H5BFB8nodkXaR3DDlSq5larWyz7fZVwWnYkqohw++0DWKqob4H5m1fYY/s9WOA3VGQj1KwIPLPg8D2eUrqv07i/Mh/r66oFD4FTS6bz/BCE7fsMFtTdO4sQ33xXwnOh4U80hYdlqq1aFcqlwixlEiSR7l+NhLWV/hl/fduw==";
        boolean verify = RSAUtils.verify(data.getBytes(), key, sign);
        System.out.println(verify);
    }

    @Test
    void a1() {
        AfterSaveEntity afterSaveEntity = authService.featureCodeAuth("123");
        System.out.println(afterSaveEntity);
    }

    @Test
    void testKey() throws Exception {
        String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxRvZKFrPETugazAKb6RyuIWAd1UeK3dKIb7wjTp4y7DFdPmCjeNzaGBwMOTU2CCDWS2I7592vXw3zkYsf9jMmKU8YMWUz2ASP1QgNzXewasgrkE0sGygYJ3hE4XiM8bMdDR26rCUhDeCF/9guZUQsIcCiTZ3oaLED0Nj4WRRWwXex0pVS8Unm3929AloZ0BjS/y/xyvZB7geehDYh/lc4kjaQ2dAyZMjEwhLc0adDWEtjfGxNsxUS9gVTOld9pyD7BOTlqtbas7rNmTWcK0GOIGN073fzy0TJpvqbsUFPuDC7n6wEjEx5hKTlhAADSLqgEloyuloJAf9zcGs9FJuwwIDAQAB";
        String sign = "O8tDwhDipYtXly81UXNG0g2yrI/m2caCaxIbtbFNXcgJgsB6DI1MNOx0FWF8LA7x8BoydYinPQDrDoeIYRV8NQq9XSuW0I0fGbI0jOX0fnIcJUl3Mg22b93DxJagJKGScpf2zRpww1HZHtRpAN3PsXGp/U7r9jzRrL0OKPLojr+u2dRUgQjQGP/EXrPngFOaibFdSxOcA8zlt6cRxxT7iYRQ1XVx7LHngGyn2vDPTP0zk2K6A+AMkrvIknBjviJIYyfGQ6exifdmILg7O6EJ5WRP0/gI+hDOl2SW48sLNgDEdvO0GHsa9vmyNqRJLshXtIOZLRWbuLngaFYLB09e9Q==";
        RSAUtils.verify("123".getBytes(), key, sign);
    }
}
