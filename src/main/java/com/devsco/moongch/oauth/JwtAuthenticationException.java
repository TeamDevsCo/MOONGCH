package com.devsco.moongch.oauth;

public class JwtAuthenticationException extends RuntimeException {
  public JwtAuthenticationException(String message, Throwable cause){
    super(generateMessage(message,cause), cause);
  }

  private static String generateMessage(String message , Throwable cause) {
    if (cause instanceof io.jsonwebtoken.ExpiredJwtException) {
      return "Expired JWT token: " + cause.getMessage();
    } else if (cause instanceof io.jsonwebtoken.UnsupportedJwtException) {
      return "Unsupported JWT token: " + cause.getMessage();
    } else if (cause instanceof io.jsonwebtoken.MalformedJwtException) {
      return "Malformed JWT token: " + cause.getMessage();
    } else if (cause instanceof io.jsonwebtoken.security.SignatureException) {
      return "Invalid JWT signature: " + cause.getMessage();
    } else if (cause instanceof IllegalArgumentException) {
      return "JWT token is null or empty: " + cause.getMessage();
    }
    return message + ": " + cause.getMessage();
  }
}
