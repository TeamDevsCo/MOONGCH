package com.devsco.moongch.oauth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class JwtAuthenticationExceptionTest {

  @Test
  @DisplayName("JwtProvider 예외 발생 시, JwtAuthenticationEntryPoint를 통해 401 응답 전파 테스트")
  void testJwtProviderException() throws ServletException, IOException {
    JwtProvider jwtProvider = mock(JwtProvider.class);
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider);
    JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    given(request.getServletPath()).willReturn("/protected");
    given(request.getHeader("Authorization")).willReturn("Bearer invalidToken");
    given(jwtProvider.extractJwtToken(request)).willReturn("invalidToken");
    given(jwtProvider.validateToken("invalidToken"))
      .willThrow(new JwtAuthenticationException(new Exception("Test error")));

    try {
      filter.doFilterInternal(request, response, filterChain);
      fail("Expected JwtAuthenticationException to be thrown");
    } catch (JwtAuthenticationException ex) {
      entryPoint.commence(request, response, ex);
      assertThat(ex).isInstanceOf(JwtAuthenticationException.class);
      assertThat(ex.getMessage()).contains("Test error");
      assertThat(ex.getCause()).isNotNull().hasMessage("Test error");

      then(response).should().sendError(eq(HttpServletResponse.SC_UNAUTHORIZED),
        argThat(message -> message.contains("Test error")));
    }

    SecurityContextHolder.clearContext();
  }
}
