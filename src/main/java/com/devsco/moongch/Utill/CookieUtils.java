package com.devsco.moongch.Utill;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

public class CookieUtils {
  /**
   * 요청에서 특정 이름의 쿠키를 찾는다.
   */
  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          return Optional.of(cookie);
        }
      }
    }
    return Optional.empty();
  }

  /**
   * 쿠키를 생성해 응답에 추가한다.
   * @param response HttpServletResponse
   * @param name 쿠키 이름
   * @param value 쿠키 값
   * @param maxAge 쿠키 유효 시간(초)
   */
  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");          // 모든 경로에서 쿠키 사용
    cookie.setHttpOnly(true);     // 자바스크립트 접근 방지 (XSS 대비)
    // cookie.setSecure(true);    // HTTPS 환경에서만 전송 (개발 시에는 주석 처리 가능)
    cookie.setMaxAge(maxAge);     // 만료 시간 설정
    response.addCookie(cookie);
  }

  /**
   * 특정 이름의 쿠키를 삭제(만료)한다.
   */
  public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) return;

    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(name)) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      }
    }
  }

  /**
   * 객체를 직렬화 후 Base64로 인코딩한 문자열로 반환한다.
   */
  public static String serialize(Object obj) {
    byte[] serialized = SerializationUtils.serialize(obj);
    return Base64.getUrlEncoder().encodeToString(serialized);
  }

  /**
   * 쿠키에 저장된 직렬화+Base64 인코딩 문자열을 역직렬화해 객체로 복원한다.
   */
  public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());
    Object deserialized = SerializationUtils.deserialize(data);
    return cls.cast(deserialized);
  }
}
