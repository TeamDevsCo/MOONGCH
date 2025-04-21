package com.devsco.moongch.common.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2Service extends DefaultOAuth2UserService {

  private final OAuth2UserInfoFactory oAuth2UserInfoFactory;
  private final RestTemplate restTemplate;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
    log.info("OAuth2 userInfo from {}: {}", registrationId, oAuth2User.getAttributes());

    OAuth2UserInfo userInfo =
      oAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

    Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
    if ("github".equals(registrationId)) {
      String accessToken = oAuth2UserRequest.getAccessToken().getTokenValue();
      String email = fetchGithubPrimaryEmail(accessToken);
      attributes.put("email", email);
    } else {
      attributes.put("email", userInfo.getEmail());
    }

    String userNameAttr = oAuth2UserRequest.getClientRegistration()
      .getProviderDetails()
      .getUserInfoEndpoint()
      .getUserNameAttributeName();

    return new DefaultOAuth2User(
      oAuth2User.getAuthorities(),
      attributes,
      userNameAttr
    );
  }

  /**
   * GitHub의 /user/emails 엔드포인트를 호출하여
   * primary & verified 이메일을 찾아 반환합니다.
   */
  private String fetchGithubPrimaryEmail(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
      "https://api.github.com/user/emails",
      HttpMethod.GET,
      entity,
      new ParameterizedTypeReference<>() {
      }
    );

    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      return null;
    }

    return response.getBody().stream()
      .filter(e -> Boolean.TRUE.equals(e.get("primary"))
        && Boolean.TRUE.equals(e.get("verified")))
      .map(e -> (String) e.get("email"))
      .findFirst()
      .orElse(null);
  }
}
