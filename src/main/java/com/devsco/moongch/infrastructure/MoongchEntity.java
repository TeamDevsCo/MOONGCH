package com.devsco.moongch.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "moongch", name = "moongch")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MoongchEntity {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;
  private Long usersId;
  private String title;
  private String description;
  private Boolean isTemp;
  private Boolean isOrigin;
  @Enumerated(EnumType.STRING)
  private Type type;

  @Getter
  public enum Type {
    TEAM, PERSONAL
  }
}
