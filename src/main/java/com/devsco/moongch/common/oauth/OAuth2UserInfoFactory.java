package com.devsco.moongch.common.oauth;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class OAuth2UserInfoFactory {
  private final Map<String, OAuth2UserInfoParser> parserMap;

  public OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
    OAuth2UserInfoParser parser = parserMap.get(registrationId.toLowerCase());
    log.info("registrationId: {},  parserMap.get : {}", registrationId, parserMap.get(registrationId));

    if (parser == null) {
      throw new UnsupportedOperationException("지원하지 않는 로그인 방식입니다. " + registrationId);
    }
    return parser.parse(attributes);
  }
}
