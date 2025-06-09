package com.devsco.moongch.infrastructure;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "moongch", name = "tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TagEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  public static TagEntity of(com.devsco.moongch.domain.Tag domain) {
    return TagEntity.builder()
      .id(domain.getId())
      .name(domain.getName())
      .build();
  }

  public com.devsco.moongch.domain.Tag toDomain() {
    com.devsco.moongch.domain.Tag tag = com.devsco.moongch.domain.Tag.builder()
      .name(this.name)
      .build();
    tag.setId(this.id);
    return tag;
  }
}
