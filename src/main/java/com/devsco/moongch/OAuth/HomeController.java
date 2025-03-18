package com.devsco.moongch.OAuth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping("/home")
  public ResponseEntity<?> home(@AuthenticationPrincipal OAuth2User oauth2User) {
    if (oauth2User == null) {
      return ResponseEntity.ok("로그인한 사용자가 없습니다.");
    } else {
      return ResponseEntity.ok("로그인한 사용자 정보: " + oauth2User.getAttributes());
    }
  }
}
