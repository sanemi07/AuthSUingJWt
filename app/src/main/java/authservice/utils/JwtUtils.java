package authservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public class JwtUtils {
    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;
    @Value("${spring.security.jwt.expiration}")
    private  int jwtExpiration;
    @Value("${spring.security.jwt.refreshtokenexpiration}")
    private  int refreshTokenExpiration;

    public String generateAccessToken(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(SignatureAlgorithm.HS256,jwtSecret)
                .compact();
    }
    public String generateRefreshToken(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis()+refreshTokenExpiration))
                .signWith(SignatureAlgorithm.HS256,jwtSecret)
                .compact();
    }
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        return !Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
