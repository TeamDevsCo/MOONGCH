package com.devsco.moongch.common.config;

import com.devsco.moongch.OAuth.*;
import com.devsco.moongch.Utill.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@Log4j2
public class SecurityConfig {

  private final CustomOAuth2Service customOAuth2Service;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                 JwtProvider jwtProvider,
                                                 JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
    http
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .csrf(AbstractHttpConfigurer::disable)
      .cors(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .logout(logout -> logout.logoutUrl("/perform_logout"))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/oauth2/authorization/**", "/login/oauth2/**","/logout").permitAll()
        .anyRequest().authenticated()
      )
      .oauth2Login(oauth2 -> oauth2
        .authorizationEndpoint(authorization -> authorization
          .authorizationRequestRepository(cookieAuthorizationRequestRepository())
        )
        .userInfoEndpoint(userInfo -> userInfo
          .userService(customOAuth2Service)
        )
        .successHandler((request, response, authentication) -> {
          log.info(">>> OAuth2 successHandler triggered!");
          OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
          String email = oauth2User.getAttributes().get("email").toString();
          String jwt = jwtProvider.createToken(email);
          log.info("AddCookie before");
          CookieUtils.addCookie(response, "JWT_TOKEN", jwt, 3600);
          response.sendRedirect("/home");
        })
      )
      .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));
    return http.build();
  }

  @Bean
  public AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }
}
