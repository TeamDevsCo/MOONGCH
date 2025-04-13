package com.devsco.moongch.common.oauth;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomOAuth2Service extends DefaultOAuth2UserService {

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    // 기본 사용자 정보 불러오기
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

    log.info("OAuth2 userInfo : {}", oAuth2User.getAttributes());

    return oAuth2User;
  }
}
