package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Moongch {
  private Long id;
  private final String title;
  private final String description;
  private final List<Snippets> snippets = new ArrayList<>();

  @Builder
  private Moongch(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public static Moongch create(String title, String description, List<Snippets> snippets) {
    Moongch moongch = Moongch.builder()
      .title(title)
      .description(description)
      .build();
    moongch.getSnippets().addAll(snippets);
    return moongch;
  }
}
