package com.transsion.daconsole.infrastructure.admin;

import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.UUID;

@Data
@Slf4j
@Configuration
public class JwtOperator {

    @Value("${platform.admin.jwt}")
    private String jwtKey;

    @Value("${platform.admin.jwt.ttl}")
    private long TTL;


    /**
     * @param key 需要加密的键值对（键）
     * @param value 需要加密的键值对（值）
     * @return 加密过后的Jwt字符串
     */
    public String getJwtToken(String key, String value) {
        //当前时间
        Date now = new Date(System.currentTimeMillis());
        //jwt加密配置
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(UUID.randomUUID().toString())//设置ID，唯一即可
                .setIssuedAt(now)//签发时间
                // .setSubject("jwtTest")//说明
                // .setIssuer("jiakang.chen")//签发者信息
                // .setAudience("xiaoming")//接收人信息
                .compressWith(CompressionCodecs.GZIP)//数据压缩方式
                .signWith(SignatureAlgorithm.HS256, jwtKey)//加密方式
                .setExpiration(new Date(now.getTime() + TTL))
                .claim(key, value);
        return jwtBuilder.compact();
    }

    /**
     * @param token 需要解密的Jwt字符串
     * @return 返回解密过后的数据体
     */
    public Claims parseToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (Exception e) {
            log.info("登录Jwt过期了,拒绝登陆");
            return null;
        }
    }
}
