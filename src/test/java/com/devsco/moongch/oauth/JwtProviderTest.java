package com.devsco.moongch.oauth;

import com.devsco.moongch.common.oauth.JwtCode;
import com.devsco.moongch.common.oauth.JwtProperties;
import com.devsco.moongch.common.oauth.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.BDDAssertions.then;

class JwtProviderTest {
  JwtProvider jwtProvider;

  @BeforeEach
  void setUp() throws Exception {
    JwtProperties testProperties = JwtProperties.builder()
      .secret("IfMLNo1OipdK3lVTc8zcW3Fqwwm0WSLMsFqsKG6xTdg=")
      .expiration(Duration.ofHours(1))
      .header("Authorization")
      .prefix("Bearer")
      .issuer("moongch.com")
      .type("JWT")
      .algorithm("HS256")
      .refresh(JwtProperties.Refresh.builder()
        .expiration(Duration.ofHours(24))
        .header("Refresh").build())
      .build();
    jwtProvider = new JwtProvider(testProperties);
  }

  @Test
  @DisplayName("Token 생성 테스트")
  public void shouldCreateValidAccessToken() {
    // Given: 테스트용 이메일
    String email = "test@gmail.com";

    // When: JWT 토큰 생성
    String token = jwtProvider.createToken(email);

    // Then: 생성된 토큰이 유효한 토큰임을 검증
    then(jwtProvider.validateToken(token)).isEqualTo(JwtCode.ACCESS);
  }

  @Test
  @DisplayName("Token 이메일 추출 테스트")
  public void shouldExtractEmailFromToken() {
    // Given: 테스트용 이메일
    String email = "test@gmail.com";

    // When: JWT 토큰 생성 후 이메일 추출
    String token = jwtProvider.createToken(email);
    String extractedEmail = jwtProvider.getEmailFromToken(token);

    // Then: 추출된 이메일이 입력한 이메일과 동일해야 함
    then(extractedEmail).isEqualTo(email);
  }
}
