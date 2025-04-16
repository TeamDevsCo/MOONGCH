package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Moongch {
  private Long id;
  private final String title;
  private final String description;
  private final List<Snippets> snippets = new ArrayList<>();

  @Builder
  public Moongch(String description, String title) {
    this.description = description;
    this.title = title;
  }
}
