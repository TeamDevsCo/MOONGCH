package com.devsco.moongch.common.oauth;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@Builder
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
  String secret,
  String prefix,
  String header,
  String issuer,
  String type,
  String algorithm,
  Duration expiration,
  Refresh refresh
) {

  @Builder
  public record Refresh(
    Duration expiration,
    String header
  ) {
  }
}
