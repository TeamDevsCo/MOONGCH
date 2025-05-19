package com.devsco.moongch.common.oauth;

import com.devsco.moongch.common.oauth.utill.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class HttpCookieOAuth2AuthorizationRequestRepositoryTest {

  private static final String TEST_AUTH_URI = "https://example.com/auth";
  private static final String TEST_CLIENT_ID = "client-id";
  private static final String TEST_REDIRECT_URI = "https://app.com/callback";
  private static final String TEST_STATE = "test-state";
  private static final String TEST_REDIRECT_PARAM = "http://example.com/redirect";
  private static final String SERIALIZED_AUTH_REQUEST = "serialized-auth-request";
  private static final int COOKIE_EXPIRE_SECONDS = 180;

  private HttpCookieOAuth2AuthorizationRequestRepository repository;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private OAuth2AuthorizationRequest mockAuthRequest;

  @BeforeEach
  @DisplayName("테스트 사전 설정")
  void setUp() {
    repository = new HttpCookieOAuth2AuthorizationRequestRepository();
    mockRequest = mock(HttpServletRequest.class);
    mockResponse = mock(HttpServletResponse.class);
    mockAuthRequest = OAuth2AuthorizationRequest.authorizationCode()
      .authorizationUri(TEST_AUTH_URI)
      .clientId(TEST_CLIENT_ID)
      .redirectUri(TEST_REDIRECT_URI)
      .state(TEST_STATE)
      .build();
  }

  @Nested
  @DisplayName("loadAuthorizationRequest 메서드 테스트")
  class LoadAuthorizationRequestTests {

    @Test
    @DisplayName("쿠키가 존재할 때 인증 요청을 정상적으로 로드해야 함")
    void whenCookieExists_shouldReturnAuthRequest() {
      // Given
      Cookie cookie = new Cookie(HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME, SERIALIZED_AUTH_REQUEST);
      Cookie[] cookies = {cookie};

      when(mockRequest.getCookies()).thenReturn(cookies);

      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        cookieUtilsMock.when(() -> CookieUtils.getCookie(mockRequest, HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME))
          .thenReturn(Optional.of(cookie));
        cookieUtilsMock.when(() -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
          .thenReturn(mockAuthRequest);

        // When
        OAuth2AuthorizationRequest result = repository.loadAuthorizationRequest(mockRequest);

        // Then
        assertEquals(mockAuthRequest, result);
        assertEquals(TEST_AUTH_URI, result.getAuthorizationUri());
        assertEquals(TEST_CLIENT_ID, result.getClientId());
      }
    }

    @Test
    @DisplayName("쿠키가 존재하지 않을 때 null을 반환해야 함")
    void whenCookieDoesNotExist_shouldReturnNull() {
      // Given
      when(mockRequest.getCookies()).thenReturn(new Cookie[0]);

      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        cookieUtilsMock.when(() -> CookieUtils.getCookie(mockRequest, HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME))
          .thenReturn(Optional.empty());

        // When
        OAuth2AuthorizationRequest result = repository.loadAuthorizationRequest(mockRequest);

        // Then
        assertNull(result);
      }
    }

    @Test
    @DisplayName("쿠키가 null일 때 null을 반환해야 함")
    void whenCookiesAreNull_shouldReturnNull() {
      // Given
      when(mockRequest.getCookies()).thenReturn(null);

      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        cookieUtilsMock.when(() -> CookieUtils.getCookie(mockRequest, HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME))
          .thenReturn(Optional.empty());

        // When
        OAuth2AuthorizationRequest result = repository.loadAuthorizationRequest(mockRequest);

        // Then
        assertNull(result);
      }
    }
  }

  @Nested
  @DisplayName("saveAuthorizationRequest 메서드 테스트")
  class SaveAuthorizationRequestTests {

    @Test
    @DisplayName("인증 요청이 null이 아닐 때 쿠키를 저장해야 함")
    void whenAuthRequestIsNotNull_shouldSaveCookies() {
      // Given
      when(mockRequest.getParameter(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME))
        .thenReturn(TEST_REDIRECT_PARAM);

      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        cookieUtilsMock.when(() -> CookieUtils.serialize(mockAuthRequest)).thenReturn(SERIALIZED_AUTH_REQUEST);

        // When
        repository.saveAuthorizationRequest(mockAuthRequest, mockRequest, mockResponse);

        // Then
        cookieUtilsMock.verify(() -> CookieUtils.addCookie(
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME),
          eq(SERIALIZED_AUTH_REQUEST),
          eq(COOKIE_EXPIRE_SECONDS)
        ));
        cookieUtilsMock.verify(() -> CookieUtils.addCookie(
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME),
          eq(TEST_REDIRECT_PARAM),
          eq(COOKIE_EXPIRE_SECONDS)
        ));
      }
    }

    @Test
    @DisplayName("리다이렉트 URI가 null일 때 해당 쿠키를 저장하지 않아야 함")
    void whenRedirectUriIsNull_shouldNotSaveRedirectCookie() {
      // Given
      when(mockRequest.getParameter(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME))
        .thenReturn(null);

      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        cookieUtilsMock.when(() -> CookieUtils.serialize(mockAuthRequest)).thenReturn(SERIALIZED_AUTH_REQUEST);

        // When
        repository.saveAuthorizationRequest(mockAuthRequest, mockRequest, mockResponse);

        // Then
        cookieUtilsMock.verify(() -> CookieUtils.addCookie(
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME),
          eq(SERIALIZED_AUTH_REQUEST),
          eq(COOKIE_EXPIRE_SECONDS)
        ));
        cookieUtilsMock.verify(() -> CookieUtils.addCookie(
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME),
          eq(null),
          eq(COOKIE_EXPIRE_SECONDS)
        ), never());
      }
    }

    @Test
    @DisplayName("인증 요청이 null일 때 쿠키를 삭제해야 함")
    void whenAuthRequestIsNull_shouldRemoveCookies() {
      // Given
      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        // When
        repository.saveAuthorizationRequest(null, mockRequest, mockResponse);

        // Then
        cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(
          eq(mockRequest),
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME)
        ));
        cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(
          eq(mockRequest),
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
        ));
      }
    }
  }

  @Nested
  @DisplayName("removeAuthorizationRequest 메서드 테스트")
  class RemoveAuthorizationRequestTests {

    @Test
    @DisplayName("인증 요청 제거 시 쿠키를 삭제하고 원래 요청을 반환해야 함")
    void shouldRemoveCookiesAndReturnRequest() {
      // Given
      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        cookieUtilsMock.when(() -> CookieUtils.getCookie(mockRequest, HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME))
          .thenReturn(Optional.of(new Cookie("oauth2_auth_request", "data")));
        cookieUtilsMock.when(() -> CookieUtils.deserialize(any(Cookie.class), eq(OAuth2AuthorizationRequest.class)))
          .thenReturn(mockAuthRequest);

        // When
        OAuth2AuthorizationRequest result = repository.removeAuthorizationRequest(mockRequest, mockResponse);

        // Then
        cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(
          eq(mockRequest),
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME)
        ));
        cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(
          eq(mockRequest),
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
        ));
        assertEquals(mockAuthRequest, result);
      }
    }

    @Test
    @DisplayName("쿠키가 없을 때 null을 반환해야 함")
    void whenNoCookie_shouldReturnNull() {
      // Given
      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        cookieUtilsMock.when(() -> CookieUtils.getCookie(mockRequest, HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME))
          .thenReturn(Optional.empty());

        // When
        OAuth2AuthorizationRequest result = repository.removeAuthorizationRequest(mockRequest, mockResponse);

        // Then
        cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(
          eq(mockRequest),
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME)
        ));
        cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(
          eq(mockRequest),
          eq(mockResponse),
          eq(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
        ));
        assertNull(result);
      }
    }
  }

  @Nested
  @DisplayName("통합 테스트")
  class IntegrationTests {

    @Test
    @DisplayName("실제 객체를 사용한 저장 및 로드 테스트")
    void shouldSaveAndLoadAuthRequest() {

      MockHttpServletRequest realRequest = new MockHttpServletRequest();
      MockHttpServletResponse realResponse = new MockHttpServletResponse();
      realRequest.setParameter(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME, TEST_REDIRECT_PARAM);


      repository.saveAuthorizationRequest(mockAuthRequest, realRequest, realResponse);


      Cookie[] cookies = realResponse.getCookies();
      assertNotNull(cookies);
      assertEquals(2, cookies.length);


      boolean foundAuthCookie = false;
      boolean foundRedirectCookie = false;

      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME)) {
          foundAuthCookie = true;
        } else if (cookie.getName().equals(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)) {
          foundRedirectCookie = true;
          assertEquals(TEST_REDIRECT_PARAM, cookie.getValue());
        }
      }

      assertTrue(foundAuthCookie, "인증 요청 쿠키가 생성되지 않았습니다");
      assertTrue(foundRedirectCookie, "리다이렉트 URI 쿠키가 생성되지 않았습니다");

      // When
      MockHttpServletRequest loadRequest = new MockHttpServletRequest();
      loadRequest.setCookies(cookies);

      // Then
      repository.removeAuthorizationRequest(loadRequest, realResponse);
    }

    @Test
    @DisplayName("전체 플로우 통합 테스트")
    void fullFlowIntegrationTest() {
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();

      request.setParameter(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME, TEST_REDIRECT_PARAM);
      repository.saveAuthorizationRequest(mockAuthRequest, request, response);

      Cookie[] cookies = response.getCookies();
      MockHttpServletRequest newRequest = new MockHttpServletRequest();
      newRequest.setCookies(cookies);

      try (var cookieUtilsMock = mockStatic(CookieUtils.class)) {
        cookieUtilsMock.when(() -> CookieUtils.getCookie(any(), eq(HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTH_REQUEST_COOKIE_NAME)))
          .thenReturn(Optional.of(cookies[0]));
        cookieUtilsMock.when(() -> CookieUtils.deserialize(any(), eq(OAuth2AuthorizationRequest.class)))
          .thenReturn(mockAuthRequest);

        OAuth2AuthorizationRequest loadedAuthRequest = repository.loadAuthorizationRequest(newRequest);
        assertEquals(mockAuthRequest, loadedAuthRequest);

        OAuth2AuthorizationRequest removedAuthRequest = repository.removeAuthorizationRequest(newRequest, response);
        assertEquals(mockAuthRequest, removedAuthRequest);
      }
    }
  }
}
