package com.devsco.moongch.oauth;

import com.devsco.moongch.common.oauth.KakaoOAuth2UserInfoParser;
import com.devsco.moongch.common.oauth.OAuth2UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KakaoOAuth2UserInfoParserTest {

  private final KakaoOAuth2UserInfoParser parser = new KakaoOAuth2UserInfoParser();

  @Test
  @DisplayName("정상적인 attributes 맵 입력 시 OAuth2UserInfo의 getEmail, getId, getAttributes가 예상대로 동작하는지 검증")
  void parse_returnsCorrectUserInfo() {
    // given
    Map<String, Object> kakaoAccount = Map.of(
      "email", "test@kakao.com"
    );
    Map<String, Object> attributes = Map.of(
      "id", 12345,
      "kakao_account", kakaoAccount
    );

    // when
    OAuth2UserInfo userInfo = parser.parse(attributes);

    // then
    assertThat(userInfo.getId()).isEqualTo("12345");
    assertThat(userInfo.getEmail()).isEqualTo("test@kakao.com");
    assertThat(userInfo.getAttributes()).isSameAs(attributes);
  }


  @Test
  @DisplayName("email 필드가 없을 경우 null을 반환하는지 확인")
  void parse_withNullEmail_thenReturnsNull() {
    // given
    Map<String, Object> attributes = Map.of("id", 12345);

    // when
    OAuth2UserInfo userInfo = parser.parse(attributes);

    // then
    assertThat(userInfo.getEmail()).isNull();

  }
}
