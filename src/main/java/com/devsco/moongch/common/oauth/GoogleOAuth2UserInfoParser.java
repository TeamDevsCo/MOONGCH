package com.devsco.moongch.oauth;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component("google")
public class GoogleOAuth2UserInfoParser implements OAuth2UserInfoParser {

  @Override
  public OAuth2UserInfo parse(Map<String, Object> attributes) {
    return new OAuth2UserInfo() {
      @Override
      public String getEmail() {
        return (String) attributes.get("email");
      }

      @Override
      public String getId() {
        return (String) attributes.get("sub");
      }

      @Override
      public Map<String, Object> getAttributes() {
        return attributes;
      }
    };
  }
}

