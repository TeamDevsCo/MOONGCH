package com.devsco.moongch.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class RequestLoggingFilterTest {

  private RequestLoggingFilter filter;

  @BeforeEach
  void setUp() {
    filter = new RequestLoggingFilter();
  }

  @AfterEach
  void tearDown() {
    MDC.clear();
  }

  @Test
  @DisplayName("XForward 헤더가 없으면 CLIENT_IP RemoteAddr로 사용 마지막엔 지워진다")
  void whenNoXForwardedFor_header_thenClientIpIsRemoteAddrAndMDCIsCleared() throws ServletException, IOException {
    // given
    MockHttpServletRequest req = new MockHttpServletRequest("GET", "/foo");
    req.setRemoteAddr("127.0.0.1");
    MockHttpServletResponse res = new MockHttpServletResponse();
    // when
    // FilterChain을 직접 구현하여 doFilter 호출 시 MDC를 검사
    FilterChain chain = (servletRequest, servletResponse) -> {
      String rid = MDC.get(RequestLoggingFilter.REQUEST_ID);
      String cip = MDC.get(RequestLoggingFilter.CLIENT_IP);

      // then
      assertNotNull(rid, "requestId should be set in MDC");
      assertEquals("127.0.0.1", cip, "when no X-Forwarded-For, clientIp = remoteAddr");
    };

    filter.doFilter(req, res, chain);

    assertNull(MDC.get(RequestLoggingFilter.REQUEST_ID));
    assertNull(MDC.get(RequestLoggingFilter.CLIENT_IP));
  }


  @Test
  @DisplayName("X-Forwarded-For 헤더가 있으면 첫 번째 IP로 사용, 마지막엔 지워진다")
  void whenXForwardedFor_headerExists_thenClientIpIsFromHeaderAndMDCIsCleared()
    throws ServletException, IOException {
    // given
    MockHttpServletRequest req = new MockHttpServletRequest("GET", "/foo");
    req.addHeader("X-Forwarded-For", "203.0.113.5, 10.0.0.2");
    req.setRemoteAddr("127.0.0.1");
    MockHttpServletResponse res = new MockHttpServletResponse();

    // when
    FilterChain chain = (servletRequest, servletResponse) -> {
      String rid = MDC.get(RequestLoggingFilter.REQUEST_ID);
      String cip = MDC.get(RequestLoggingFilter.CLIENT_IP);

      assertNotNull(rid, "requestId should be set in MDC");
      assertEquals("203.0.113.5", cip, "첫 번째 IP만 꺼내와야 한다");
    };

    filter.doFilter(req, res, chain);

    // then
    assertNull(MDC.get(RequestLoggingFilter.REQUEST_ID));
    assertNull(MDC.get(RequestLoggingFilter.CLIENT_IP));
  }

}
