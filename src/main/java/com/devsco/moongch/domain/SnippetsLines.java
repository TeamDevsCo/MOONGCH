package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Builder
public class SnippetsLines {
  private final Long lineNo;
  private final String text;
  private final List<ReviewStreams> reviewStreams = new ArrayList<>();
}
