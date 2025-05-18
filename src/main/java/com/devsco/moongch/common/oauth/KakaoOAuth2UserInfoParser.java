package com.devsco.moongch.common.oauth;

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
        return kakaoAccount != null ? kakaoAccount.get("email").toString() : null;
      }

      @Override
      public String getId() {
        return attributes.get("id") != null ? attributes.get("id").toString() : null;
      }

      @Override
      public Map<String, Object> getAttributes() {
        return attributes;
      }
    };
  }
}
