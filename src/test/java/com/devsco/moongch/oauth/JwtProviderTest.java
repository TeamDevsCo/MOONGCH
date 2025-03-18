package com.devsco.moongch.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JwtProviderTest {

  private JwtProvider jwtProvider;

  @BeforeEach
  void setUp() throws Exception {
    jwtProvider = new JwtProvider();
    String testSecret = "IfMLNo1OipdK3lVTc8zcW3Fqwwm0WSLMsFqsKG6xTdg=";  // Test 위한 임의의 값 생성
    Field secretField = JwtProvider.class.getDeclaredField("jwtSecret");
    secretField.setAccessible(true);
    secretField.set(jwtProvider, testSecret);
  }

  @Test
  @DisplayName("Token 생성 테스트")
  public void testAccessToken() {
    String email = "test@gmail.com";
    String token = jwtProvider.createToken(email);
    assertThat(jwtProvider.validateToken(token)).isEqualTo(JwtCode.ACCESS);
  }

  @Test
  @DisplayName("Token 이메일 추출 테스트")
  public void testRefreshToken() {
    String email = "test@gmail.com";
    String token = jwtProvider.createToken(email);
    assertThat(jwtProvider.getEmailFromToken(token)).isEqualTo(email);
  }

}
