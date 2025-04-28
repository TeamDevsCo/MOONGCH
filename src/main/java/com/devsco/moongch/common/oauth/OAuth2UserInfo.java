  package com.devsco.moongch.oauth;

  import java.util.Map;

  public interface OAuth2UserInfo {
    String getEmail();
    String getId();
    Map<String, Object> getAttributes();
  }
