package com.devsco.moongch.oauth;

import com.devsco.moongch.common.oauth.AuthController;
import com.devsco.moongch.common.oauth.utill.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // 순수 컨트롤러 테스트를 위함
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser
  @DisplayName("로그아웃 요청시 JWT 토큰 쿠키가 삭제되고 성공 메시지가 반환되는지 확인")
  void testLogout() throws Exception {
    try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class)) {
      cookieUtilsMock.when(() -> CookieUtils.deleteCookie(any(HttpServletRequest.class),
          any(HttpServletResponse.class),
          eq("JWT_TOKEN")))
        .thenAnswer(invocation -> null);

      ResultActions resultActions = mockMvc.perform(post("/logout")
        .cookie(new Cookie("JWT_TOKEN", "test-token-value"))
        .with(SecurityMockMvcRequestPostProcessors.csrf()));

      resultActions
        .andExpect(status().isOk())
        .andExpect(content().string("로그아웃 되었습니다."));

      cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(any(HttpServletRequest.class),
        any(HttpServletResponse.class),
        eq("JWT_TOKEN")));
    }
  }

  @Test
  @WithMockUser
  void testLogoutWithoutCookie() throws Exception {
    try (MockedStatic<CookieUtils> cookieUtilsMock = Mockito.mockStatic(CookieUtils.class)) {
      cookieUtilsMock.when(() -> CookieUtils.deleteCookie(any(HttpServletRequest.class),
          any(HttpServletResponse.class),
          eq("JWT_TOKEN")))
        .thenAnswer(invocation -> null);

      ResultActions resultActions = mockMvc.perform(post("/logout")
        .with(SecurityMockMvcRequestPostProcessors.csrf()));

      resultActions
        .andExpect(status().isOk())
        .andExpect(content().string("로그아웃 되었습니다."));

      cookieUtilsMock.verify(() -> CookieUtils.deleteCookie(any(HttpServletRequest.class),
        any(HttpServletResponse.class),
        eq("JWT_TOKEN")));
    }
  }
}
