package com.devsco.moongch.oauth;

public enum JwtCode {
  ACCESS,   // 토큰이 유효함
  EXPIRED,  // 토큰 만료
  DENIED    // 토큰이 유효하지 않음 (형식 오류, 서명 오류 등)
}
