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
        Object email = attributes.get("email");
        return email != null ? email.toString() : null;
      }

      @Override
      public String getId() {
        Object id = attributes.get("sub");
        return id != null ? id.toString() : null;
      }

      @Override
      public Map<String, Object> getAttributes() {
        return attributes;
      }
    };
  }
}

