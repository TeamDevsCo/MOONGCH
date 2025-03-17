package com.devsco.moongch.OAuth;

import com.devsco.moongch.Utill.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class HttpCookieOAuth2AuthorizationRequestRepository
  implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  public static final String OAUTH2_AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request";
  public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
  private static final int COOKIE_EXPIRE_SECONDS = 180;

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    // 쿠키에서 OAuth2AuthorizationRequest를 찾아 역직렬화
    return CookieUtils.getCookie(request, OAUTH2_AUTH_REQUEST_COOKIE_NAME)
      .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
      .orElse(null);
  }

  @Override
  public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
    if (authorizationRequest == null) {
      // null이면 쿠키 삭제
      removeAuthorizationRequestCookies(request, response);
      return;
    }

    // 쿠키에 OAuth2AuthorizationRequest 직렬화하여 저장
    CookieUtils.addCookie(response,
            OAUTH2_AUTH_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(authorizationRequest),
            COOKIE_EXPIRE_SECONDS);

    // 필요하다면, 리다이렉트 URI도 별도 쿠키로 저장 가능
    String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
    if (redirectUriAfterLogin != null) {
      CookieUtils.addCookie(response,
            REDIRECT_URI_PARAM_COOKIE_NAME,
            redirectUriAfterLogin,
            COOKIE_EXPIRE_SECONDS);
    }
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
    // 1) 쿠키에 저장된 요청 정보를 읽어온 뒤
    OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
    // 2) 쿠키 삭제
    removeAuthorizationRequestCookies(request, response);
    // 3) 이전에 저장된 AuthorizationRequest를 반환
    return authRequest;
  }

  private void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
    CookieUtils.deleteCookie(request, response, OAUTH2_AUTH_REQUEST_COOKIE_NAME);
    CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
  }
}
