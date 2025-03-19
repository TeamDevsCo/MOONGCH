package com.devsco.moongch.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.BDDAssertions.then;

class JwtProviderTest {

  private JwtProvider jwtProvider;

  @BeforeEach
  void setUp() throws Exception {
    jwtProvider = new JwtProvider();
    String testSecret = "IfMLNo1OipdK3lVTc8zcW3Fqwwm0WSLMsFqsKG6xTdg=";
    Field secretField = JwtProvider.class.getDeclaredField("jwtSecret");
    secretField.setAccessible(true);
    secretField.set(jwtProvider, testSecret);
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
