package com.devsco.moongch.application;

import lombok.Builder;

import java.util.List;

@Builder
public record SnippetLinesDto(
  Long lineNo,
  String text,
  List<ReviewsDto> reviews
) {}
