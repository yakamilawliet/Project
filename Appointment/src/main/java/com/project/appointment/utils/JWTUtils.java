package com.project.appointment.utils;

import com.project.appointment.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 传入用户登录信息，生成token
     * @param user
     * @return
     */
    public String generateToken(User user) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("openid", user.getOpenid());
        map.put("created", new Date());
        return this.generateJwt(map);
    }

    /**
     * 根据荷载信息去生成token
     * @param map
     * @return
     */
    private String generateJwt(Map<String, Object> map) {
        return Jwts.builder()
                .setClaims(map)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     * 根据token获取荷载信息
     * @param token
     * @return
     */
    public Claims getTokenBody(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据token判断当前时间内，该token是否过期
     * @param token
     * @return
     */
    public boolean isExpiration(String token) {
        return this.getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 刷新token令牌
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = this.getTokenBody(token);
        claims.setExpiration(new Date());
        return this.generateJwt(claims);
    }

    /**
     * 根据token获取openid
     * @param jwtToken
     * @return
     */
    public String getOpenidByToken(String jwtToken) {
        try {
            DecodedJWT jwt = JWT.decode(jwtToken);
            return jwt.getClaim("openid").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
