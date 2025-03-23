package com.devsco.moongch.oauth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
    throws ServletException, IOException {

    if (request.getServletPath().equals("/logout")) {
      log.info("request.getUri(): {}", request.getRequestURI());
      filterChain.doFilter(request, response);
      return;
    }

    String token = jwtProvider.extractJwtToken(request);

    if (token != null && jwtProvider.validateToken(token) == JwtCode.ACCESS) {
      setAuthentication(token);
    } else {
      SecurityContextHolder.clearContext();
    }
    filterChain.doFilter(request, response);
  }

  private void setAuthentication(String token) {
    String email = jwtProvider.getEmailFromToken(token);
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", email);

    DefaultOAuth2User oauth2User =
      new DefaultOAuth2User(Collections.emptyList(), attributes, "email");

    UsernamePasswordAuthenticationToken authentication =
      new UsernamePasswordAuthenticationToken(oauth2User, null, Collections.emptyList());

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
