package com.devsco.moongch.OAuth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletResponse response) {
    // JWT_TOKEN 쿠키 삭제 (Set-Cookie 헤더로 만료 처리)
    Cookie cookie = new Cookie("JWT_TOKEN", null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0); // 즉시 만료
    response.addCookie(cookie);
    return ResponseEntity.ok("로그아웃 되었습니다.");
  }
}
