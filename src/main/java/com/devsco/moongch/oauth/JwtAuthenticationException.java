package com.devsco.moongch.oauth;

public class JwtAuthenticationException extends RuntimeException {
  public JwtAuthenticationException(Throwable cause){
    super(generateMessage(cause));
  }

  private static String generateMessage(Throwable cause) {
    if (cause instanceof io.jsonwebtoken.ExpiredJwtException) {
      return cause.getMessage();
    } else if (cause instanceof io.jsonwebtoken.UnsupportedJwtException) {
      return cause.getMessage();
    } else if (cause instanceof io.jsonwebtoken.MalformedJwtException) {
      return cause.getMessage();
    } else if (cause instanceof io.jsonwebtoken.security.SignatureException) {
      return cause.getMessage();
    } else if (cause instanceof IllegalArgumentException) {
      return cause.getMessage();
    }
    return cause.getMessage();
  }
}
