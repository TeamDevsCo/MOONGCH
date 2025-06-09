package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Snippets {
  @Setter
  private Long id;
  @Setter
  private Long moongchId;
  private final String ext;
  private final List<SnippetsLine> snippetsLines;

  @Builder
  public Snippets(String ext) {
    this.ext = ext;
    this.snippetsLines = new ArrayList<>();
  }

  // 비즈니스 로직
  public void addLine(SnippetsLine line) {
    this.snippetsLines.add(line);
  }

  public void addLines(List<SnippetsLine> lines) {
    this.snippetsLines.addAll(lines);
  }

  public int getLineCount() {
    return this.snippetsLines.size();
  }

  // 팩토리 메서드
  public static Snippets from(com.devsco.moongch.application.SnippetsDto dto) {
    Snippets snippet = Snippets.builder()
      .ext(dto.ext())
      .build();

    dto.lines().forEach(lineDto -> {
      snippet.addLine(SnippetsLine.from(lineDto));
    });

    return snippet;
  }
}
