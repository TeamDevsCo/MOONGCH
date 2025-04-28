package com.devsco.moongch.oauth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OAuth2UserInfoFactoryTest {

  @Mock
  private OAuth2UserInfoParser kakaoParser;

  @Mock
  private OAuth2UserInfoParser googleParser;

  private OAuth2UserInfoFactory factory;


  @BeforeEach
  void setUp() {
    Map<String, OAuth2UserInfoParser> parserMap = Map.of(
      "kakao", kakaoParser,
      "google", googleParser
    );
    factory = new OAuth2UserInfoFactory(parserMap);
  }



  @Test
  @DisplayName("알려진 registrationId로 요청하면 해당 파서를 통해 정상적으로 OAuth2UserInfo를 반환하는지 확인")
  void whenKnownRegistration_thenReturnsParsedInfo() {
    Map<String, Object> attributes = Map.of("id", "123", "name", "Tester");
    OAuth2UserInfo expectedUserInfo = mock(OAuth2UserInfo.class);

    given(kakaoParser.parse(attributes)).willReturn(expectedUserInfo);

    OAuth2UserInfo actual = factory.getOAuth2UserInfo("kakao", attributes);

    assertThat(actual).isSameAs(expectedUserInfo);
  }




  @Test
  @DisplayName("대소문자 구분 없이 registrationId로 파서가 매핑되는지 확인")
  void whenRegistrationIdCaseMismatch_thenStillMatchesParser() {
    // given
    Map<String, Object> attrs = Map.of("id", "abc");
    OAuth2UserInfo dummyInfo = mock(OAuth2UserInfo.class);

    given(googleParser.parse(attrs)).willReturn(dummyInfo);

    // when
    OAuth2UserInfo result = factory.getOAuth2UserInfo("GOOGLE", attrs);

    // then
    assertThat(result).isSameAs(dummyInfo);
  }



  @Test
  @DisplayName("지원하지 않는 registrationId로 요청 시 UnsupportedOperationException 발생하는지 확인")
  void whenUnknownRegistration_thenThrowsException() {
    // given
    Map<String, Object> emptyAttrs = Map.of();

    // expect
    assertThatThrownBy(() ->
      factory.getOAuth2UserInfo("facebook", emptyAttrs)
    )
      .isInstanceOf(UnsupportedOperationException.class)
      .hasMessageContaining("지원하지 않는 로그인 방식");
  }
}
