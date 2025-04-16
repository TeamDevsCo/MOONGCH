package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class Snippets {
  private String ext;
  private final List<SnippetsLines> snippetsLines = new ArrayList<>();

  public static Snippets create(String ext, List<SnippetsLines> snippetsLines) {
    Snippets snippets = Snippets.builder().ext(ext).build();
    snippets.getSnippetsLines().addAll(snippetsLines);
    return snippets;
  }

  public String getFullContent() {
    return snippetsLines.stream()
      .map(SnippetsLines::getText)
      .collect(Collectors.joining("\n"));
  }
}
