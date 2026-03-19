package org.pztsblog.blogsend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    // 生成一个安全的密钥（实际开发中可以放在配置文件里）
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token 过期时间：7天 (毫秒计算)
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 根据用户 ID 和用户名生成 Token
     */
    public static String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE_TIME);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username) // 主题通常放用户名
                .claim("userId", userId) // 自定义载荷，把用户ID塞进去
                .setIssuedAt(now) // 签发时间
                .setExpiration(expiration) // 过期时间
                .signWith(key) // 签名
                .compact();
    }

    /**
     * 解析 Token 拿到里面的数据 (Claims)
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}