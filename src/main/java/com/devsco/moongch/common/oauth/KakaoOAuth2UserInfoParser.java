package com.devsco.moongch.oauth;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component("kakao")
public class KakaoOAuth2UserInfoParser implements OAuth2UserInfoParser {


  @Override
  public OAuth2UserInfo parse(Map<String, Object> attributes) {
    return new OAuth2UserInfo() {
      @Override
      public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
      }

      @Override
      public String getId() {
        return attributes.get("id") != null ? (String) attributes.get("id") : null;
      }

      @Override
      public Map<String, Object> getAttributes() {
        return attributes;
      }
    };
  }
}
