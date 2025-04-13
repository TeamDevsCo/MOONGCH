package com.devsco.moongch.common.oauth;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtKeyGenerator {
  public static void main(String[] args) {
    SecureRandom secureRandom = new SecureRandom();
    byte[] keyBytes = new byte[32];
    secureRandom.nextBytes(keyBytes);
    String base64Key = Base64.getEncoder().encodeToString(keyBytes);
  }
}
