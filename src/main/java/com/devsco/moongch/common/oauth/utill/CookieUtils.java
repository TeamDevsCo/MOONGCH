package com.devsco.moongch.common.oauth.utill;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    return Optional.ofNullable(request.getCookies()).stream().flatMap(Arrays::stream)
      .filter(cookie -> cookie.getName().equals(name))
      .findFirst();
  }


  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    // cookie.setSecure(true); // 프로덕션에서는 HTTPS 환경에서 true로 설정
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    Optional.ofNullable(request.getCookies()).stream().flatMap(Arrays::stream)
      .filter(cookie -> cookie.getName().equals(name))
      .forEach(cookie -> {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      });
  }


  public static String serialize(Object obj) {
    byte[] serialized = SerializationUtils.serialize(obj);
    return Base64.getUrlEncoder().encodeToString(serialized);
  }

  public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());
    try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
         ObjectInputStream ois = new ObjectInputStream(bais)) {
      return cls.cast(ois.readObject());
    } catch (IOException | ClassNotFoundException e) {
      throw new IllegalArgumentException("Failed to deserialize cookie data", e);
    }
  }

}
