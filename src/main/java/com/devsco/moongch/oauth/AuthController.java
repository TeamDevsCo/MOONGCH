package com.devsco.moongch.oauth;

import com.devsco.moongch.oauth.utill.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class AuthController {

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request,HttpServletResponse response) {
    CookieUtils.deleteCookie(request, response, "JWT_TOKEN");
    return ResponseEntity.ok("로그아웃 되었습니다.");
  }
}
