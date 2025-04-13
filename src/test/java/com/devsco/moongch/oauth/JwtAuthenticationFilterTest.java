package com.devsco.moongch.oauth;

import com.devsco.moongch.common.oauth.JwtAuthenticationFilter;
import com.devsco.moongch.common.oauth.JwtCode;
import com.devsco.moongch.common.oauth.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

  @Mock
  JwtProvider jwtProvider;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  FilterChain filterChain;

  @InjectMocks
  JwtAuthenticationFilter filter;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("로그아웃 엔드포인트인 경우 필터 건너뛰기 테스트")
  void testDoFilter_SkipLogoutPath() throws ServletException, IOException {
    given(request.getServletPath()).willReturn("/logout");

    filter.doFilterInternal(request, response, filterChain);

    then(filterChain).should().doFilter(request, response);
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  @DisplayName("유효한 토큰이 헤더에 있을 경우 인증 정보 설정 테스트")
  void testDoFilter_ValidTokenHeader() throws ServletException, IOException {
    String token = "validToken";
    String email = "test@example.com";

    given(request.getServletPath()).willReturn("/home");
    given(jwtProvider.extractJwtToken(request)).willReturn(token);
    given(jwtProvider.validateToken(token)).willReturn(JwtCode.ACCESS);
    given(jwtProvider.getEmailFromToken(token)).willReturn(email);

    filter.doFilterInternal(request, response, filterChain);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    assertThat(principal).isInstanceOf(DefaultOAuth2User.class);
    DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
    Map<String, Object> attributes = oauth2User.getAttributes();
    assertThat(attributes.get("email")).isEqualTo(email);
    then(filterChain).should().doFilter(request, response);
  }

  @Test
  @DisplayName("유효하지 않은 토큰이 있을 경우 SecurityContext 클리어 테스트")
  void testDoFilter_InvalidToken() throws ServletException, IOException {
    String token = "invalidToken";

    given(request.getServletPath()).willReturn("/home");
    given(jwtProvider.extractJwtToken(request)).willReturn(token);
    given(jwtProvider.validateToken(token)).willReturn(JwtCode.DENIED);

    filter.doFilterInternal(request, response, filterChain);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    then(filterChain).should().doFilter(request, response);
  }

  @Test
  @DisplayName("헤더가 없으면 쿠키에서 토큰 추출 테스트")
  void testDoFilter_TokenFromCookie() throws ServletException, IOException {
    String token = "tokenFromCookie";
    String email = "cookie@example.com";

    given(request.getServletPath()).willReturn("/home");
    given(jwtProvider.extractJwtToken(request)).willReturn(token);
    given(jwtProvider.validateToken(token)).willReturn(JwtCode.ACCESS);
    given(jwtProvider.getEmailFromToken(token)).willReturn(email);

    filter.doFilterInternal(request, response, filterChain);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    assertThat(principal).isInstanceOf(DefaultOAuth2User.class);
    DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
    Map<String, Object> attributes = oauth2User.getAttributes();
    assertThat(attributes.get("email")).isEqualTo(email);
    then(filterChain).should().doFilter(request, response);
  }
}
