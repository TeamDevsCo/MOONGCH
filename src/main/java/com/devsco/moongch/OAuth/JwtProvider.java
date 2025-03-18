package com.devsco.moongch.OAuth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.util.Date;

@Component
@Log4j2
public class JwtProvider {

  @Value("${JWT_SECRET}")
  private String jwtSecret;

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(String email) {
    Date now = new Date();
    long jwtExpirationMs = 3600000; // 1시간
    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

    return Jwts.builder()
      .setSubject(email)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public String getEmailFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
      .setSigningKey(getSigningKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
    return claims.getSubject();
  }

  /**
   * JWT 유효성 검사 메서드
   * @param token 검증할 JWT 토큰
   * @return JwtCode.ACCESS 토큰이 유효할 때, JwtCode.EXPIRED 토큰 만료, 그 외 DENIED 반환
   */
  public JwtCode validateToken(String token) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token);
      return JwtCode.ACCESS;
    } catch (ExpiredJwtException e) {
      throw new JwtAuthenticationException("JWT token validation failed",e);
    }
  }

}
