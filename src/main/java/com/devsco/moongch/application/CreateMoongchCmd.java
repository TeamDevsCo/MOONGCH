package com.devsco.moongch.application;

import com.devsco.moongch.domain.Tag;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateMoongchCmd(
  Long userId,
  String title,
  String description,
  Boolean isTemp,
  Boolean isOrigin,
  String type,
  Long originMoongchId,
  List<Tag> tags,
  List<SnippetsDto> snippets
) {}
