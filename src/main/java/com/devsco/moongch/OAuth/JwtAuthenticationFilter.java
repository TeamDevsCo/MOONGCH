package com.devsco.moongch.OAuth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String token = null;
    String authHeader = request.getHeader("Authorization");

    // 우선 헤더에서 Bearer 토큰을 확인
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
    } else {
      // 헤더가 없다면 쿠키에서 JWT_TOKEN을 확인
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if ("JWT_TOKEN".equals(cookie.getName())) {
            token = cookie.getValue();
            break;
          }
        }
      }
    }

    if (token != null && jwtProvider.validateToken(token) == JwtCode.ACCESS) {
      String email = jwtProvider.getEmailFromToken(token);
      Map<String, Object> attributes = new HashMap<>();
      attributes.put("email", email);
      DefaultOAuth2User oauth2User = new DefaultOAuth2User(Collections.emptyList(), attributes, "email");

      UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(oauth2User, null, Collections.emptyList());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } else {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }
}
