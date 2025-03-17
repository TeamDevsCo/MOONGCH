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

  // 운영환경에서는 이 값을 환경변수나 안전한 설정 파일로 관리하세요.
  @Value("${JWT_SECRET}")
  private String jwtSecret;

  // 암호학적으로 안전한 Key 생성 (Base64 인코딩된 문자열을 사용하는 것이 좋음)
  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // JWT 생성: 주제로 이메일을 사용
  public String createToken(String email) {
    Date now = new Date();
    // 1시간
    long jwtExpirationMs = 3600000;
    Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
    return Jwts.builder()
      .setSubject(email)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  // JWT에서 이메일 추출
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
    } catch (ExpiredJwtException e) {  // 토큰이 만료된 경우
      log.error("Expired JWT token: {}", e.getMessage());
      return JwtCode.EXPIRED;
    } catch (UnsupportedJwtException e) { // 지원되지 않는 토큰인 경우
      log.error("Unsupported JWT token: {}", e.getMessage());
      return JwtCode.DENIED;
    } catch (MalformedJwtException e) {  // 토큰 형식이 올바르지 않은 경우
      log.error("Malformed JWT token: {}", e.getMessage());
      return JwtCode.DENIED;
    } catch (SignatureException e) { // 서명이 유효하지 않은 경우
      log.error("Invalid JWT signature: {}", e.getMessage());
      return JwtCode.DENIED;
    } catch (IllegalArgumentException e) { // 토큰 문자열이 비어있거나 잘못된 경우
      log.error("JWT token is null or empty: {}", e.getMessage());
      return JwtCode.DENIED;
    } catch (Exception e) {
      log.error("JWT token validation failed: {}", e.getMessage());
      return JwtCode.DENIED;
    }
  }

}
