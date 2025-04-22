package com.devsco.moongch.common.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Log4j2
public class RequestLoggingFilter implements Filter {

  public static final String REQUEST_ID = "requestId";

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;

    String requestId = UUID.randomUUID().toString().substring(0,8);
    MDC.put(REQUEST_ID, requestId);

    String clientIp = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
      .map(h -> h.split(",")[0].trim())
      .orElse(request.getRemoteAddr());
    MDC.put("clientIp", clientIp);


    try {
      log.info("[{}] Incoming Request: {} {} {}?{}",
        requestId,clientIp,
        request.getMethod(),
        request.getRequestURI(),
        request.getQueryString() == null ? "" : request.getQueryString());

      chain.doFilter(req, res);
    } finally {
      MDC.remove(REQUEST_ID);
      MDC.remove(clientIp);
    }
  }

}
