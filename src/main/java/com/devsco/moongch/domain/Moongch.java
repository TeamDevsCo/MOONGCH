package com.devsco.moongch.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Moongch {
  @Setter
  private Long id;
  private final Long usersId;

  private final String title;
  private final String description;

  private final Boolean isTemp;
  private final Boolean isOrigin;

  private final Type type;
  private final Long originMoongchId;

  private final List<Snippets> snippets;
  private final List<Tag> tags;


  @Builder
  public Moongch(
    Long usersId,
    String title,
    String description,
    Boolean isTemp,
    Boolean isOrigin,
    Type type,
    Long originMoongchId,
    List<Tag> tags
  ) {
    this.usersId         = usersId;
    this.title           = title;
    this.description     = description;
    this.isTemp          = isTemp;
    this.isOrigin        = isOrigin;
    this.type            = type;
    this.originMoongchId = originMoongchId;
    this.snippets = new ArrayList<>();
    this.tags     = tags != null
      ? new ArrayList<>(tags)
      : new ArrayList<>();
  }

  @Getter
  public enum Type {
    TEAM, PERSONAL
  }

}
