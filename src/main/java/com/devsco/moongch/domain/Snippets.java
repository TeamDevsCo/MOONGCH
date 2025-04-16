package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Snippets {
  private Long id;
  private final String ext;
  private final List<SnippetsLine> snippetsLines = new ArrayList<>();

  @Builder
  public Snippets(String ext) {
    this.ext = ext;
  }

  public static Snippets create(String ext, List<SnippetsLine> snippetsLines) {
    Snippets snippets = Snippets.builder().ext(ext).build();
    snippets.getSnippetsLines().addAll(snippetsLines);
    return snippets;
  }

  public String getFullContent() {
    return snippetsLines.stream()
      .map(SnippetsLine::getText)
      .collect(Collectors.joining("\n"));
  }
}
