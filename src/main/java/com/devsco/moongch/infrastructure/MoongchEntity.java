package com.devsco.moongch.infrastructure;

import com.devsco.moongch.domain.Moongch;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "moongch", name = "moongch")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access =AccessLevel.PRIVATE)
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
  Long originMoongchId;

  @Getter
  public enum Type {
    TEAM, PERSONAL
  }

  public static MoongchEntity of(Moongch dom) {
    return MoongchEntity.builder()
      .id(dom.getId())
      .usersId(dom.getUsersId())
      .title(dom.getTitle())
      .description(dom.getDescription())
      .isTemp(dom.getIsTemp())
      .isOrigin(dom.getIsOrigin())
      .type(Type.valueOf(dom.getType().name()))
      .originMoongchId(dom.getOriginMoongchId())
      .build();
  }

  public Moongch toDomain() {
    Moongch dom = Moongch.builder()
      .usersId(this.usersId)
      .title(this.title)
      .description(this.description)
      .isTemp(this.isTemp)
      .isOrigin(this.isOrigin)
      .type(Moongch.Type.valueOf(this.type.name()))
      .originMoongchId(this.originMoongchId)
      .build();
    dom.setId(this.id);
    return dom;
  }
}
