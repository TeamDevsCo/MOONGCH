package com.devsco.moongch.common.oauth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtProperties jwtProperties;

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(String email) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtProperties.expiration().toMillis());

    return Jwts.builder()
      .setSubject(email)
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(getSigningKey(), SignatureAlgorithm.forName(jwtProperties.algorithm()))
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
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
      throw new JwtAuthenticationException(e);
    }
  }

  public String extractJwtToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(jwtProperties.header()))
      .filter(auth -> auth.startsWith(jwtProperties.prefix()+" "))
      .map(auth -> auth.substring(7))
      .orElseGet(() -> getCookieValue(request));
  }


  private String getCookieValue(HttpServletRequest request) {
    return Optional.ofNullable(request.getCookies())
      .stream()
      .flatMap(Arrays::stream)
      .filter(cookie -> "JWT_TOKEN".equals(cookie.getName()))
      .map(Cookie::getValue)
      .findFirst()
      .orElse(null);
  }

}
