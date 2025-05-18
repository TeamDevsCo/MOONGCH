package com.devsco.moongch.oauth;

import com.devsco.moongch.common.oauth.GoogleOAuth2UserInfoParser;
import com.devsco.moongch.common.oauth.OAuth2UserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class GoogleOAuth2UserInfoParserTest {

  private final GoogleOAuth2UserInfoParser parser = new GoogleOAuth2UserInfoParser();


  @Test
  @DisplayName("정상적인 attributes 맵 입력 시 OAuth2UserInfo의 getEmail, getId, getAttributes가 예상대로 동작하는지 검증")
  void parse_returnsCorrectUserInfo() {
    // given
    Map<String, Object> attributes = Map.of(
      "sub", "13",
      "email", "test@example.com"
    );

    // when
    OAuth2UserInfo userInfo = parser.parse(attributes);

    // then
    assertThat(userInfo.getId()).isEqualTo("13");
    assertThat(userInfo.getEmail()).isEqualTo("test@example.com");
    assertThat(userInfo.getAttributes()).isSameAs(attributes);
  }


  @Test
  @DisplayName("email 필드가 없을 경우 getEmail은 null을 반환하는지 확인")
  public void test() throws Exception{
    // given
    Map<String, Object> attributes = Map.of("id", 99);

    // when
    OAuth2UserInfo userInfo = parser.parse(attributes);

    // then
    assertThat(userInfo.getEmail()).isNull();
  }
}
