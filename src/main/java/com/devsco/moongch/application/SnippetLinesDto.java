package com.devsco.moongch.application;

import java.util.List;

public record SnippetLinesDto(
  Long lineNo,
  String text,
  List<ReviewsDto> reviews
) {
}
