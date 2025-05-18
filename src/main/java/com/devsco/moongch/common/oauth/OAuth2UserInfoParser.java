package com.devsco.moongch.common.oauth;

import java.util.Map;

public interface OAuth2UserInfoParser {
  OAuth2UserInfo parse(Map<String, Object> attributes);
}
