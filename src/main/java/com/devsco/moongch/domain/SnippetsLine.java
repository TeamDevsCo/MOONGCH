package com.devsco.moongch.domain;

import com.devsco.moongch.application.SnippetLinesDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SnippetsLine {
  @Setter
  private Long id;
  @Setter
  private Long snippetsId;
  private final Long lineNo;
  private final String text;

  @Builder
  public SnippetsLine(Long lineNo, String text) {
    this.lineNo = lineNo;
    this.text = text;
  }

  // 팩토리 메서드
  public static SnippetsLine from(SnippetLinesDto dto) {
    return SnippetsLine.builder()
      .lineNo(dto.lineNo())
      .text(dto.text())
      .build();
  }
}
