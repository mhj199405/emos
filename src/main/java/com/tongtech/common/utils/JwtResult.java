package com.tongtech.common.utils;


import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtResult {

    private long expire = 3000000;//PropertyUtil.getProperty("base.jwt.expire", (long) 3000000);

    private String secret = "kOcUdrzitZvYOIJZOWoWulOYpapIQbcO";//PropertyUtil.getProperty("base.jwt.secret", "Liby");

    private String token;
    private String username;
    private String uniqueId;
    private String permissions;
    private String features;
    private String[] others;
    private String roles;
    private Claims claims;

    public void setExpire(Long expire) {
        this.expire = expire;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public JwtResult(String uniqueId, String subject, String permsStr, String roleStr, String featureStr, String... params) {
        this.uniqueId = uniqueId;
        this.username = subject;
        this.permissions = permsStr;
        this.roles = roleStr;
        this.features = featureStr;
        this.others = params;
    }

    public static JwtResult build(String uniqueId, String subject, String permsStr, String roleStr, String featureStr, String... params) {
        return new JwtResult(uniqueId, subject, permsStr, roleStr, featureStr, params);
    }

    public String getSign() {
        JwtBuilder builder = Jwts.builder();
        if (uniqueId == null) {
            uniqueId = UUID.randomUUID().toString();
        }
        builder.setId(uniqueId);
        if (username == null) {
            username = "";
        }
        builder.setSubject(username);
        if (permissions != null && !permissions.isEmpty()) {
            builder.claim("permissions", permissions);
        }
        if (roles != null && !roles.isEmpty()) {
            builder.claim("roles", roles);
        }
        if (features != null && !features.isEmpty()) {
            builder.claim("features", features);
        }
        if (others != null && others.length > 0) {
            builder.claim("others", JSON.toJSONString(others));
        }
        Date expiresDate = new Date(System.currentTimeMillis() + expire);
        builder.setExpiration(expiresDate).signWith(SignatureAlgorithm.HS256, secret);
        return builder.compact();
    }

    public JwtResult(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        this.token = token;
        claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String getId() {
        if (claims == null) {
            return null;
        }
        return claims.getId();
    }

    public String getSubject() {
        if (claims == null) {
            return null;
        }
        return claims.getSubject();
    }

    public List<String> getPermissions() {
        if (claims == null) {
            return null;
        }
        Object obj = claims.get("permissions");
        if (obj == null) {
            return null;
        }
        List<String> result = JSON.parseArray((String) obj, String.class);
        return result;
    }

    public List<String> getRoles() {
        if (claims == null) {
            return null;
        }
        Object obj = claims.get("roles");
        if (obj == null) {
            return null;
        }
        List<String> result = JSON.parseArray((String) obj, String.class);
        return result;
    }

    public List<String> getFeatures() {
        if (claims == null) {
            return null;
        }
        Object obj = claims.get("features");
        if (obj == null) {
            return null;
        }
        List<String> result = JSON.parseArray((String) obj, String.class);
        return result;
    }

    public boolean isExistFeature(String key) {
        List<String> features = getFeatures();
        if (features == null) {
            return false;
        }
        if (features.indexOf(key) < 0) {
            return false;
        }
        return true;
    }

    public List<String> getOthers() {
        if (claims == null) {
            return null;
        }
        Object obj = claims.get("others");
        if (obj == null) {
            return null;
        }
        List<String> result = JSON.parseArray((String) obj, String.class);
        return result;
    }

    public String getOther(int index) {
        if (index < 0) {
            return null;
        }
        List<String> result = getOthers();
        if (index >= result.size()) {
            return null;
        }
        return result.get(index);
    }

    public static JwtResult build(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return new JwtResult(token);
    }

    public String refresh() {
        if (claims == null) {
            return null;
        }
        Date expireDate = claims.getExpiration();
        long diff = expireDate.getTime() - (new Date()).getTime();
        if (diff > expire / 3) {
            return null;
        }
        Date expiresDate = new Date(System.currentTimeMillis() + expire);
        claims.setExpiration((expiresDate));
        JwtBuilder builder = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secret);
        String token = builder.compact();
        return token;
    }

    public JwtResult() {

    }

    public static JwtResult build() throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if (token == null) {
            return new JwtResult();
        }
        if (token.isEmpty()) {
            return new JwtResult();
        }
        return new JwtResult(token);
    }

}
