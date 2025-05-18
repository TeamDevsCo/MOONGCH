package com.devsco.moongch.common.oauth;

import java.util.Map;

public interface OAuth2UserInfo {
  String getEmail();

  String getId();

  Map<String, Object> getAttributes();
}
