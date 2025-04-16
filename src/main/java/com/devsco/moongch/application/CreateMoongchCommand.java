package com.devsco.moongch.application;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateMoongchCommand(
  String title,
  String description,
  List<SnippetsDto> snippets
) { }
