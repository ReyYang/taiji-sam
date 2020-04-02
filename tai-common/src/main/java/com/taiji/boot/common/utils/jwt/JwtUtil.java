package com.taiji.boot.common.utils.jwt;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Demo JwtUtil
 *
 * @author ydy
 * @date 2020/3/28 20:19
 */
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * 密钥
     */
    private static final String SECRET = "5e7f420c7e5da8fb72aa4b6c";

    /**
     * 默认字段key:exp
     */
    private static final String EXP = "exp";

    /**
     * 默认字段key:payload
     */
    private static final String PAYLOAD = "payload";

    /**
     * 过期时间
     */
    private static final long EXPIRATION = 3600L;

    /**
     * 生成用户token,设置token超时时间
     */
    public static <T> String createToken(T object, long maxTime) {
        Date expireDate;
        if (maxTime != 0) {
            //过期时间
            expireDate = new Date(System.currentTimeMillis() + maxTime * 1000);
        } else {
            expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                .withHeader(map)// 添加头部
                .withClaim("data", JSON.toJSONString(object))
                .withExpiresAt(expireDate) //超时设置,设置过期的日期
                .withIssuedAt(new Date()) //签发时间
                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
        return token;
    }

    /**
     * 校验token并解析token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("token解码异常");
            //解码异常则抛出异常
            return null;
        }
        return jwt.getClaims();
    }


}
