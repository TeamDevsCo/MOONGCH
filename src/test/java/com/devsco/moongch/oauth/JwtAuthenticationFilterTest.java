package com.devsco.moongch.oauth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

  @Mock
  private JwtProvider jwtProvider;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @InjectMocks
  private JwtAuthenticationFilter filter;

  @BeforeEach
  void setUp() {
    // 테스트 시작 전 SecurityContext를 클리어합니다.
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("로그아웃 엔드포인트인 경우 필터 건너뛰기 테스트")
  void testDoFilter_SkipLogoutPath() throws ServletException, IOException {
    // Given: /logout 경로인 경우
    when(request.getServletPath()).thenReturn("/logout");

    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then: 필터 체인을 그대로 호출하고, SecurityContext는 비워져 있어야 합니다.
    verify(filterChain).doFilter(request, response);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  @DisplayName("유효한 토큰이 헤더에 있을 경우 인증 정보 설정 테스트")
  void testDoFilter_ValidTokenHeader() throws ServletException, IOException {
    // Given: /home 경로와 Authorization 헤더에 유효한 토큰이 있음
    String token = "validToken";
    String email = "test@example.com";
    when(request.getServletPath()).thenReturn("/home");
    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(jwtProvider.validateToken(token)).thenReturn(JwtCode.ACCESS);
    when(jwtProvider.getEmailFromToken(token)).thenReturn(email);

    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then: SecurityContextHolder에 인증 정보가 설정되어야 합니다.
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    assertThat(principal).isInstanceOf(DefaultOAuth2User.class);
    DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
    Map<String, Object> attributes = oauth2User.getAttributes();
    assertThat(attributes.get("email")).isEqualTo(email);
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("유효하지 않은 토큰이 있을 경우 SecurityContext 클리어 테스트")
  void testDoFilter_InvalidToken() throws ServletException, IOException {
    // Given: /home 경로와 Authorization 헤더에 유효하지 않은 토큰이 있음
    String token = "invalidToken";
    when(request.getServletPath()).thenReturn("/home");
    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(jwtProvider.validateToken(token)).thenReturn(JwtCode.DENIED);

    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then: SecurityContextHolder에 인증 정보가 없어야 합니다.
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(filterChain).doFilter(request, response);
  }

  @Test
  @DisplayName("헤더가 없으면 쿠키에서 토큰 추출 테스트")
  void testDoFilter_TokenFromCookie() throws ServletException, IOException {
    // Given: /home 경로, Authorization 헤더는 null, 쿠키에 JWT_TOKEN이 존재함
    String token = "tokenFromCookie";
    String email = "cookie@example.com";
    when(request.getServletPath()).thenReturn("/home");
    when(request.getHeader("Authorization")).thenReturn(null);
    Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
    when(request.getCookies()).thenReturn(new Cookie[] { jwtCookie });
    when(jwtProvider.validateToken(token)).thenReturn(JwtCode.ACCESS);
    when(jwtProvider.getEmailFromToken(token)).thenReturn(email);

    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then: 쿠키로부터 토큰을 추출하여 인증 정보가 설정되어야 합니다.
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    assertThat(principal).isInstanceOf(DefaultOAuth2User.class);
    DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
    Map<String, Object> attributes = oauth2User.getAttributes();
    assertThat(attributes.get("email")).isEqualTo(email);
    verify(filterChain).doFilter(request, response);
  }
}
