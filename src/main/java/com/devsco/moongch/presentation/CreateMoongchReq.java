package com.devsco.moongch.presentation;

import com.devsco.moongch.application.SnippetsDto;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateMoongchReq (
  Long userId,
  String title,
  String description,
  Boolean isTemp,
  Boolean isOrigin,
  String type,
  Long originMoongchId,
  List<String> tagNames,
  List<SnippetsDto> snippets
){}
