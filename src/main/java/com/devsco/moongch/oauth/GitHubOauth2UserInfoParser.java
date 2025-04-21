package com.devsco.moongch.oauth;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component("github")
public class GitHubOauth2UserInfoParser implements OAuth2UserInfoParser {

  @Override
  public OAuth2UserInfo parse(Map<String, Object> attributes) {
    return new OAuth2UserInfo() {
      @Override
      public String getEmail() {
        Object email = attributes.get("name");
        return email != null ? email.toString() : null;
      }

      @Override
      public String getId() {
        return attributes.get("id").toString();
      }

      @Override
      public Map<String, Object> getAttributes() {
        return attributes;
      }
    };
  }
}
