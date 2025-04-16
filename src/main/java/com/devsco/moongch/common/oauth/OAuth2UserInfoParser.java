package com.devsco.moongch.oauth;

import java.util.Map;

public interface OAuth2UserInfoParser {
  OAuth2UserInfo parse(Map<String, Object> attributes);
}
