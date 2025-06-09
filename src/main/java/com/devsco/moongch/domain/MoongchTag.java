package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Setter;

@Builder
public class MoongchTag {
  @Setter
  private Long id;
  private final Long moongchId;
  private final String tagId;

  @Builder
  public MoongchTag(Long moongchId, String tagId) {
    this.moongchId = moongchId;
    this.tagId = tagId;
  }
}
