package com.devsco.moongch.common.oauth;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class CustomOAuth2Service extends DefaultOAuth2UserService {

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
    log.info("OAuth2 userInfo from {}: {}", registrationId, oAuth2User.getAttributes());

    OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

    Map<String, Object> attributes = new HashMap<>(userInfo.getAttributes());
    attributes.put("email", userInfo.getEmail());

    return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "id");
  }
}
