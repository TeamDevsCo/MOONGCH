package com.devsco.moongch.application;

import java.util.List;

public record SnippetsDto(
  String ext,
  List<SnippetLinesDto> lines
) { }
