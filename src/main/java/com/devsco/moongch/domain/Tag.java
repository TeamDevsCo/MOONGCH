package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Tag {

  @Setter
  private Long id;
  private final String name;

  @Builder
  public Tag(String name) {
    this.name = name;
  }
}
