package com.devsco.moongch.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(schema = "moongch", name = "snippets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SnippetsEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long moongchId;
  private String title;
  @Column(columnDefinition = "text")
  private String contents;
  private String ext;
}
